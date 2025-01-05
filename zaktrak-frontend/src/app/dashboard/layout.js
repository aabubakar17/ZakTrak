"use client";

import {
  LayoutDashboard,
  PiggyBank,
  Calculator,
  BarChart3,
  CreditCard,
  User,
} from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import ProtectedRoute from "@/components/auth/ProtectedRoute";
import clsx from "clsx";

const sidebarLinks = [
  { name: "Overview", href: "/dashboard", icon: LayoutDashboard },
  { name: "Assets", href: "/dashboard/assets", icon: PiggyBank },
  { name: "Payments", href: "/dashboard/payments", icon: CreditCard },
  { name: "Profile", href: "/dashboard/profile", icon: User },
];

export default function DashboardLayout({ children }) {
  const pathname = usePathname();

  return (
    <ProtectedRoute>
      <div className="min-h-screen flex">
        {/* Sidebar */}
        <aside className="w-64 bg-white border-r border-slate-200">
          <nav className="p-4 space-y-2">
            {sidebarLinks.map((link) => {
              const Icon = link.icon;
              return (
                <Link
                  key={link.href}
                  href={link.href}
                  className={clsx(
                    "flex items-center space-x-3 px-3 py-2 rounded-lg text-sm font-medium transition-colors",
                    pathname === link.href
                      ? "bg-emerald-50 text-emerald-600"
                      : "text-slate-600 hover:bg-slate-50"
                  )}
                >
                  <Icon className="h-5 w-5" />
                  <span>{link.name}</span>
                </Link>
              );
            })}
          </nav>
        </aside>

        {/* Main Content */}
        <main className="flex-1 bg-slate-50">
          <div className="p-8">{children}</div>
        </main>
      </div>
    </ProtectedRoute>
  );
}
