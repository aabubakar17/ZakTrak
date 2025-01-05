"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { authAPI } from "@/lib/api";

export default function ProtectedRoute({ children }) {
  const router = useRouter();

  useEffect(() => {
    if (!authAPI.isAuthenticated()) {
      router.push("/login");
    }
  }, [router]);

  return children;
}
