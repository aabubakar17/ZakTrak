"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";

const PaymentHistoryPreview = ({ payments }) => {
  return (
    <Card className="p-6">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-lg font-semibold">Recent Payments</h2>
        <Button
          variant="outline"
          size="sm"
          onClick={() => router.push("/dashboard/payments")}
        >
          View All
        </Button>
      </div>
      <div className="space-y-4">
        {payments.slice(0, 3).map((payment) => (
          <div key={payment.id} className="flex justify-between items-center">
            <div>
              <p className="font-medium">${payment.amount.toLocaleString()}</p>
              <p className="text-sm text-slate-600">{payment.description}</p>
            </div>
            <p className="text-sm text-slate-600">{payment.paymentDate}</p>
          </div>
        ))}
      </div>
    </Card>
  );
};

export default PaymentHistoryPreview;
