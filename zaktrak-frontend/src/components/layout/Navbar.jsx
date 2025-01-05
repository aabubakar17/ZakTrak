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
import { useRouter, usePathname } from "next/navigation";

const Navbar = () => {
  const router = useRouter();
  const pathname = usePathname();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(localStorage.getItem("user").firstName);

  useEffect(() => {
    checkAuthStatus();
  }, [pathname]); // Re-check when pathname changes

  const checkAuthStatus = () => {
    const isAuth = authAPI.isAuthenticated();
    setIsAuthenticated(isAuth);
    if (isAuth) {
      const userData = localStorage.getItem("user");
      if (userData) {
        setUser(JSON.parse(userData));
      }
    }
  };

  const handleLogout = () => {
    authAPI.logout();
    setIsAuthenticated(false);
    setUser(null);
    router.push("/login");
  };
  return (
    <nav className="w-full sticky top-0 bg-transparent border-b border-slate-100">
      <div className="max-w-8xl pb-4 mx-auto px-4 lg:px-8">
        <div className="flex justify-between h-16 items-center ">
          {/* Logo */}
          <Link href="/" className="flex items-center flex-shrink-0">
            <img
              className="w-auto h-32" // Adjusted height
              src="/ZakTrak-logo.png"
              alt="ZakTrak Logo"
            />
          </Link>
          {/* Auth Buttons */}
          <div className="flex items-center pt-4 space-x-4">
            {!isAuthenticated ? (
              <>
                <Link href="/login">
                  <Button variant="outline">Login</Button>
                </Link>
                <Link href="/register">
                  <Button className="bg-emerald-600 hover:bg-emerald-700">
                    {" "}
                    Sign Up
                  </Button>
                </Link>
              </>
            ) : (
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="outline" className="flex items-center gap-2">
                    <User className="h-4 w-4" />
                    <span>{user?.firstName || "My Account"}</span>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                  <DropdownMenuItem onClick={() => router.push("/dashboard")}>
                    Dashboard
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
