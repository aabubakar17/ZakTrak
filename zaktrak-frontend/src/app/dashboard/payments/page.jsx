"use client";

import { useState, useEffect } from "react";
import api from "@/lib/api";
import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Loader2, Plus, Pencil, Trash2 } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

export default function PaymentsPage() {
  const [payments, setPayments] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingPayment, setEditingPayment] = useState(null);
  const [formData, setFormData] = useState({
    amount: "",
    description: "",
  });

  useEffect(() => {
    loadPayments();
  }, []);

  const loadPayments = async () => {
    try {
      const response = await api.get("/zakat-payments");
      setPayments(response);
    } catch (error) {
      console.error("Error loading payments:", error);
    }
  };

  const handleSubmit = async () => {
    setIsLoading(true);
    try {
      if (editingPayment) {
        await api.put(
          `/zakat-payments/payments/${editingPayment.id}`,
          formData
        );
      } else {
        await api.post("/zakat-payments", formData);
      }
      await loadPayments();
      setIsDialogOpen(false);
      setFormData({ amount: "", description: "" });
      setEditingPayment(null);
    } catch (error) {
      console.error("Error saving payment:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async (paymentId) => {
    try {
      await api.delete(`/zakat-payments/payments/${paymentId}`);
      await loadPayments();
    } catch (error) {
      console.error("Error deleting payment:", error);
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Zakat Payments</h1>
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
            <Button
              onClick={() => {
                setEditingPayment(null);
                setFormData({ amount: "", description: "" });
              }}
              className="flex-left bg-emerald-600 hover:bg-emerald-700"
            >
              <Plus className="h-4 w-4 mr-2" />
              Record Payment
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>
                {editingPayment ? "Edit Payment" : "Record New Payment"}
              </DialogTitle>
            </DialogHeader>
            <div className="space-y-4">
              <div>
                <Label>Amount</Label>
                <div className="relative">
                  <span className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-500">
                    £
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
              <div>
                <Label>Description</Label>
                <Textarea
                  value={formData.description}
                  onChange={(e) =>
                    setFormData((prev) => ({
                      ...prev,
                      description: e.target.value,
                    }))
                  }
                  placeholder="Add details about this payment"
                />
              </div>
              <Button
                onClick={handleSubmit}
                disabled={isLoading}
                className="w-full bg-emerald-600 hover:bg-emerald-700"
              >
                {isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Saving...
                  </>
                ) : (
                  "Save Payment"
                )}
              </Button>
            </div>
          </DialogContent>
        </Dialog>
      </div>
      <Card className="p-4">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Date</TableHead>
              <TableHead>Amount</TableHead>
              <TableHead>Description</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {payments.map((payment) => (
              <TableRow key={payment.id}>
                <TableCell>{payment.paymentDate}</TableCell>
                <TableCell className="font-medium">
                  £{payment.amount.toLocaleString()}
                </TableCell>
                <TableCell>{payment.description}</TableCell>
                <TableCell className="space-x-2">
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => {
                      setEditingPayment(payment);
                      setFormData({
                        amount: payment.amount.toString(),
                        description: payment.description,
                      });
                      setIsDialogOpen(true);
                    }}
                  >
                    <Pencil className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => handleDelete(payment.id)}
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Card>
    </div>
  );
}
