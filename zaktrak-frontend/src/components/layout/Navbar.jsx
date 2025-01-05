"use client";

import Link from "next/link";
import { Button } from "@/components/ui/button";
import { authAPI } from "@/lib/api";
import { useEffect, useState } from "react";
import { User, LogOut } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useRouter } from "next/navigation";

const Navbar = () => {
  const router = useRouter();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);

  useEffect(() => {
    setUser(authAPI.getUser());
    setIsAuthenticated(authAPI.isAuthenticated());
  }, []);

  const handleLogout = () => {
    authAPI.logout();
    setIsAuthenticated(false);
    router.push("/login");
  };

  return (
    <nav className="w-full sticky top-0 bg-slate-50 border-b border-slate-100">
      <div className="max-w-8xl pb-4 mx-auto px-4 lg:px-8">
        <div className="flex justify-between h-16 items-center ">
          {/* Logo */}
          <Link href="/" className="flex items-center flex-shrink-0">
            <img
              className="w-auto h-40" // Adjusted height
              src="/ZakTrak-logo.png"
              alt="ZakTrak Logo"
            />
          </Link>
          {/* Navigation Links */}
          <div className="hidden md:flex items-center space-x-4">
            <Link href="/" className="text-slate-600 hover:text-slate-900">
              Home
            </Link>
            {isAuthenticated && (
              <>
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
              </>
            )}
          </div>

          {/* Auth Buttons */}
          <div className="flex items-center pt-4 space-x-4">
            {!isAuthenticated ? (
              <>
                <Link href="/login">
                  <Button variant="outline">Login</Button>
                </Link>
                <Link href="/register">
                  <Button>Sign Up</Button>
                </Link>
              </>
            ) : (
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="outline" className="flex items-center gap-2">
                    <User className="h-4 w-4" />
                    <span>{user?.fullName || "My Account"}</span>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                  <DropdownMenuItem
                    onClick={() => router.push("/dashboard/profile")}
                  >
                    Profile
                  </DropdownMenuItem>
                  <DropdownMenuItem onClick={handleLogout}>
                    <LogOut className="h-4 w-4 mr-2" />
                    Logout
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
