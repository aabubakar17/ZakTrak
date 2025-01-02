"use client";

import Link from "next/link";
import { Button } from "@/components/ui/button";

const Navbar = () => {
  return (
    <nav className="w-full sticky top-0 bg-slate-50 border-b border-slate-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          {/* Logo */}
          <Link href="/" className="flex items-center">
            <span className="text-2xl font-bold text-slate-900">ZakTrak</span>
          </Link>

          {/* Navigation Links */}
          <div className="hidden md:flex items-center space-x-4">
            <Link href="/" className="text-slate-600 hover:text-slate-900">
              Home
            </Link>
            <Link
              href="/dashboard"
              className="text-slate-600 hover:text-slate-900"
            >
              Dashboard
            </Link>
            <Link
              href="/zakat-summary"
              className="text-slate-600 hover:text-slate-900"
            >
              Summary
            </Link>
            <Link
              href="/tracker"
              className="text-slate-600 hover:text-slate-900"
            >
              Tracker
            </Link>
          </div>

          {/* Auth Buttons */}
          <div className="flex items-center space-x-4">
            <Link href="/login">
              <Button variant="outline">Login</Button>
            </Link>
            <Link href="/register">
              <Button>Sign Up</Button>
            </Link>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
