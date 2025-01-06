"use client";

import { useState, useEffect } from "react";
import api from "@/lib/api";
import CurrencySelector from "@/components/CurrencySelector";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
import { Textarea } from "@/components/ui/textarea";
import { Loader2, Pencil, Trash2, Plus } from "lucide-react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  getLocalStorage,
  setLocalStorage,
  removeLocalStorage,
} from "@/lib/localStorage";

const assetCategories = [
  {
    id: "CASH_AND_SAVINGS",
    label: "Cash Savings",
    description: "Include bank accounts, cash on hand, and fixed deposits",
  },
  {
    id: "INVESTMENTS",
    label: "Investments",
    description: "Stocks, mutual funds, and other investment vehicles",
  },
  {
    id: "GOLD_AND_SILVER",
    label: "Gold/Jewelry",
    description: "Value of gold, silver, and precious metals",
  },
  {
    id: "BUSINESS_ASSETS",
    label: "Business Assets",
    description: "Current business assets and inventory",
  },
];

export default function AssetsPage() {
  const [selectedCategory, setSelectedCategory] = useState("CASH_AND_SAVINGS");
  const [currency, setCurrency] = useState("GBP");
  const [isLoading, setIsLoading] = useState(false);
  const [assets, setAssets] = useState({});
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [formData, setFormData] = useState({
    amount: "",
    notes: "",
  });

  useEffect(() => {
    loadAssets();
    const savedCurrency = getLocalStorage("preferredCurrency");
    if (savedCurrency) setCurrency(savedCurrency);
  }, []);

  const loadAssets = async () => {
    try {
      const response = await api.get("/assets");
      if (response && Array.isArray(response)) {
        const groupedAssets = response.reduce((acc, asset) => {
          if (!acc[asset.type]) acc[asset.type] = [];
          acc[asset.type].push(asset);
          return acc;
        }, {});
        setAssets(groupedAssets);
      }
    } catch (error) {
      console.error("Error loading assets:", error);
    }
  };

  const handleDelete = async (assetId) => {
    try {
      await api.delete(`/assets/${assetId}`);
      await loadAssets();
    } catch (error) {
      console.error("Error deleting asset:", error);
    }
  };

  const handleSave = async () => {
    setIsLoading(true);
    try {
      await api.post("/assets", {
        type: selectedCategory,
        description: formData.notes,
        value: parseFloat(formData.amount),
        zakatable: true,
      });

      await loadAssets();
      setIsDialogOpen(false);
      setFormData({ amount: "", notes: "" });
    } catch (error) {
      console.error("Error saving asset:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const currencySymbols = {
    USD: "$",
    GBP: "£",
    EUR: "€",
    AED: "د.إ",
  };

  return (
    <div className="max-w-6xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Asset Management</h1>
        <CurrencySelector value={currency} onValueChange={setCurrency} />
      </div>

      <Tabs value={selectedCategory} onValueChange={setSelectedCategory}>
        <TabsList className="grid grid-cols-4 w-full">
          {assetCategories.map((category) => (
            <TabsTrigger key={category.id} value={category.id}>
              {category.label}
            </TabsTrigger>
          ))}
        </TabsList>

        {assetCategories.map((category) => (
          <TabsContent key={category.id} value={category.id}>
            <Card className="p-6">
              <div className="flex justify-between items-center mb-6">
                <div>
                  <h2 className="text-lg font-semibold">{category.label}</h2>
                  <p className="text-sm text-slate-600">
                    {category.description}
                  </p>
                </div>
                <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                  <DialogTrigger asChild>
                    <Button
                      onClick={() => {
                        setFormData({ amount: "", notes: "" });
                      }}
                      className="bg-emerald-600 hover:bg-emerald-700"
                    >
                      <Plus className="h-4 w-4 mr-2" />
                      Add Asset
                    </Button>
                  </DialogTrigger>
                  <DialogContent>
                    <DialogHeader>
                      <DialogTitle>{"Add New Asset"}</DialogTitle>
                    </DialogHeader>
                    <div className="space-y-4">
                      <div className="space-y-2">
                        <Label>Amount</Label>
                        <div className="relative">
                          <span className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-500">
                            {currencySymbols[currency]}
                          </span>
                          <Input
                            type="number"
                            className="pl-8"
                            value={formData.amount}
                            onChange={(e) =>
                              setFormData((prev) => ({
                                ...prev,
                                amount: e.target.value,
                              }))
                            }
                          />
                        </div>
                      </div>
                      <div className="space-y-2">
                        <Label>Notes</Label>
                        <Textarea
                          value={formData.notes}
                          onChange={(e) =>
                            setFormData((prev) => ({
                              ...prev,
                              notes: e.target.value,
                            }))
                          }
                        />
                      </div>
                      <Button
                        className="w-full bg-emerald-600 hover:bg-emerald-700"
                        onClick={handleSave}
                        disabled={isLoading}
                      >
                        {isLoading ? (
                          <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                            Saving...
                          </>
                        ) : (
                          "Save Asset"
                        )}
                      </Button>
                    </div>
                  </DialogContent>
                </Dialog>
              </div>

              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Amount</TableHead>
                    <TableHead>Notes</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {assets[category.id]?.map((asset) => (
                    <TableRow key={asset.id}>
                      <TableCell className="font-medium">
                        {currencySymbols[currency]}
                        {asset.value.toLocaleString()}
                      </TableCell>
                      <TableCell>{asset.description}</TableCell>
                      <TableCell className="space-x-2">
                        <Button
                          variant="outline"
                          size="icon"
                          onClick={() => handleDelete(asset.id)}
                        >
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </Card>
          </TabsContent>
        ))}
      </Tabs>
    </div>
  );
}
