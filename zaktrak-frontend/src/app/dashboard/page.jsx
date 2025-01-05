"use client";

import { useEffect, useState } from "react";
import api from "@/lib/api";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Progress } from "@/components/ui/progress";
import { Wallet, Calculator, ArrowUpRight, AlertCircle } from "lucide-react";
import { useRouter } from "next/navigation";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Label } from "@/components/ui/label";
import Joyride, { STATUS } from "react-joyride";
import ZakatCharities from "@/components/ZakatCharities";
import { LoadingSpinner } from "@/components/loading";

export default function DashboardPage() {
  const router = useRouter();
  const [user, setUser] = useState(null);
  const [nisabType, setNisabType] = useState("silver");
  const [runTour, setRunTour] = useState(false);
  const [payments, setPayments] = useState([]);
  const [zakatInfo, setZakatInfo] = useState({
    totalDue: 0,
    totalPaid: 0,
    remaining: 0,
    isBelowNisab: false,
  });
  const [assetSummary, setAssetSummary] = useState({
    CASH_AND_SAVINGS: 0,
    INVESTMENTS: 0,
    GOLD_AND_SILVER: 0,
    BUSINESS_ASSETS: 0,
  });
  const [isLoading, setIsLoading] = useState(true);

  const tourSteps = [
    {
      target: ".nisab-selector",
      content:
        "Select your preferred Nisab calculation method - Gold or Silver. This determines the minimum threshold for Zakat calculation based on current market values.",
      placement: "bottom",
    },
    {
      target: ".manage-assets-btn",
      content:
        "Click here to add, edit, or remove your assets across different categories like cash, investments, gold, and business assets.",
      placement: "left",
    },
    {
      target: ".progress-card",
      content:
        "This card shows your Zakat progress, including total amount due, amount paid, and remaining balance.",
      placement: "bottom",
    },
    {
      target: ".view-payments-btn",
      content:
        "View your complete payment history and manage all your Zakat payments.",
      placement: "left",
    },
    {
      target: ".recent-payments",
      content: "Quick overview of your most recent Zakat payments.",
      placement: "left",
    },
    {
      target: ".asset-summary",
      content:
        "Overview of all your assets by category. This helps track your wealth and calculate Zakat accurately.",
      placement: "top",
    },
    {
      target: ".zakat-charities",
      content:
        "Choose from trusted Zakat collection organizations to fulfill your Zakat obligation.",
      placement: "bottom",
    },
  ];

  useEffect(() => {
    loadDashboardData();
  }, [nisabType]);

  const loadDashboardData = async () => {
    setIsLoading(true);
    try {
      const [
        calculateResponse,
        totalPaidResponse,
        assetsResponse,
        paymentsResponse,
        userResponse,
      ] = await Promise.all([
        api.get(`zakat/calculate/${nisabType}`),
        api.get("zakat-payments/total"),
        api.get("assets"),
        api.get("zakat-payments"),
        api.get("user/"),
      ]);

      setUser(userResponse);
      localStorage.setItem("user", JSON.stringify(userResponse));

      const totalDue = calculateResponse;
      const totalPaid = totalPaidResponse;
      const remaining = totalDue - totalPaid;
      const totalAssets = assetsResponse.reduce(
        (sum, asset) => sum + asset.value,
        0
      );

      setPayments(paymentsResponse);
      setZakatInfo({
        totalDue,
        totalPaid,
        remaining,
        isBelowNisab: totalAssets < NISAB_VALUES[nisabType].total,
      });

      const summary = assetsResponse.reduce((acc, asset) => {
        acc[asset.type] = (acc[asset.type] || 0) + asset.value;
        return acc;
      }, {});
      setAssetSummary(summary);
    } catch (error) {
      console.error("Error loading dashboard data:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const NISAB_VALUES = {
    silver: { grams: 600, pricePerGram: 0.95, total: 600 * 0.95 },
    gold: { grams: 85, pricePerGram: 85, total: 85 * 85 },
  };

  const progressPercentage = zakatInfo.totalDue
    ? Math.min((zakatInfo.totalPaid / zakatInfo.totalDue) * 100, 100)
    : 0;

  return (
    <div className="max-w-sm md:max-w-7xl   p-2 space-y-6">
      {isLoading && <LoadingSpinner />}
      {/* Header Section */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="flex justify-between items-center mb-6">
          <div>
            <h1 className="text-2xl font-bold">
              Welcome back, {user?.firstName || "User"}
            </h1>
            <p className="text-slate-600 mt-2">
              Track and manage your Zakat obligations
            </p>
            <Button
              className="mt-2"
              variant="outline"
              onClick={() => setRunTour(true)}
            >
              Take Tour
            </Button>
          </div>
        </div>
        <RadioGroup
          value={nisabType}
          onValueChange={setNisabType}
          className="flex gap-4 justify-end nisab-selector"
        >
          <div className="flex items-center space-x-2">
            <RadioGroupItem value="silver" id="silver" />
            <Label htmlFor="silver">Silver Nisab</Label>
          </div>
          <div className="flex items-center space-x-2">
            <RadioGroupItem value="gold" id="gold" />
            <Label htmlFor="gold">Gold Nisab</Label>
          </div>
        </RadioGroup>
      </div>

      {/* Nisab Alert */}
      {zakatInfo.isBelowNisab && (
        <Alert>
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>
            Your total assets (£{assetSummary.totalAssets?.toLocaleString()})
            are below the {nisabType} nisab threshold (£
            {NISAB_VALUES[nisabType].total.toLocaleString()}).
          </AlertDescription>
        </Alert>
      )}

      {/* Main Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Zakat Summary */}
        <Card className="lg:col-span-2 p-6 progress-card">
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold">Zakat Progress</h2>
              <Button
                onClick={() => router.push("/dashboard/assets")}
                variant="outline"
                className="manage-assets-btn"
              >
                Manage Assets
              </Button>
            </div>
            <div className="grid grid-cols-3 gap-4">
              <div>
                <p className="text-sm text-slate-600">Total Due</p>
                <p className="text-2xl font-bold">
                  £{zakatInfo.totalDue.toLocaleString()}
                </p>
              </div>
              <div>
                <p className="text-sm text-slate-600">Paid</p>
                <p className="text-2xl font-bold text-emerald-600">
                  £{zakatInfo.totalPaid.toLocaleString()}
                </p>
              </div>
              <div>
                <p className="text-sm text-slate-600">Remaining</p>
                <p className="text-2xl font-bold text-amber-600">
                  £{Math.max(zakatInfo.remaining.toLocaleString(), 0)}
                </p>
              </div>
            </div>
            <Progress value={progressPercentage} />
            <p className="text-sm text-slate-600">
              {progressPercentage.toFixed(1)}% of your zakat obligation
              fulfilled
            </p>
          </div>
        </Card>

        {/* Recent Payments */}
        <Card className="p-6 recent-payments">
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold">Recent Payments</h2>
              <Button
                onClick={() => router.push("/dashboard/payments")}
                variant="outline"
                size="sm"
                className="view-payments-btn"
              >
                View All
              </Button>
            </div>
            <div className="space-y-3 ">
              {payments
                .slice(payments.length - 3)
                .reverse()
                .map((payment) => (
                  <div
                    key={payment.id}
                    className="flex justify-between items-center py-2 border-b"
                  >
                    <div>
                      <p className="font-medium">
                        £{payment.amount.toLocaleString()}
                      </p>
                      <p className="text-sm text-slate-600">
                        {payment.description}
                      </p>
                    </div>
                    <p className="text-sm text-slate-600">
                      {new Date(payment.createdAt).toLocaleDateString()}
                    </p>
                  </div>
                ))}
            </div>
          </div>
        </Card>
      </div>

      {/* Asset Summary Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 asset-summary">
        <Card className="p-4">
          <div className="flex justify-between">
            <div>
              <p className="text-sm text-slate-600">Cash & Savings</p>
              <p className="text-xl font-bold mt-1">
                £{assetSummary.CASH_AND_SAVINGS?.toLocaleString() || "0"}
              </p>
            </div>
            <Wallet className="h-5 w-5 text-emerald-600" />
          </div>
        </Card>

        <Card className="p-4">
          <div className="flex justify-between">
            <div>
              <p className="text-sm text-slate-600">Investments</p>
              <p className="text-xl font-bold mt-1">
                £{assetSummary.INVESTMENTS?.toLocaleString() || "0"}
              </p>
            </div>
            <ArrowUpRight className="h-5 w-5 text-emerald-600" />
          </div>
        </Card>

        <Card className="p-4">
          <div className="flex justify-between">
            <div>
              <p className="text-sm text-slate-600">Gold & Silver</p>
              <p className="text-xl font-bold mt-1">
                £{assetSummary.GOLD_AND_SILVER?.toLocaleString() || "0"}
              </p>
            </div>
            <Calculator className="h-5 w-5 text-emerald-600" />
          </div>
        </Card>

        <Card className="p-4">
          <div className="flex justify-between">
            <div>
              <p className="text-sm text-slate-600">Business Assets</p>
              <p className="text-xl font-bold mt-1">
                £{assetSummary.BUSINESS_ASSETS?.toLocaleString() || "0"}
              </p>
            </div>
            <Calculator className="h-5 w-5 text-emerald-600" />
          </div>
        </Card>
      </div>

      {/* Zakat Charities */}
      <div className="zakat-charities">
        <ZakatCharities />
      </div>

      <Joyride
        steps={tourSteps}
        run={runTour}
        continuous
        showProgress
        showSkipButton
        styles={{
          options: {
            arrowColor: "#fff",
            backgroundColor: "#fff",
            overlayColor: "rgba(0, 0, 0, 0.5)",
            primaryColor: "#10b981",
            textColor: "#1f2937",
            width: 300,
            zIndex: 1000,
          },
        }}
        callback={({ status }) => {
          if ([STATUS.FINISHED, STATUS.SKIPPED].includes(status)) {
            setRunTour(false);
          }
        }}
      />
    </div>
  );
}
