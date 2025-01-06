"use client";

import { Button } from "@/components/ui/button";
import Link from "next/link";
import { ArrowRight } from "lucide-react";

const Hero = () => {
  return (
    <div className="relative isolate overflow-hidden">
      <div className="absolute inset-x-0 top-0 h-64 bg-gradient-to-b from-emerald-50 to-transparent" />

      <div className="mx-auto max-w-7xl px-6 pb-6 pt-16 sm:pt-32 lg:px-8">
        <div className="mx-auto max-w-3xl text-center">
          <h1 className="mt-6 text-4xl font-bold tracking-tight sm:text-6xl">
            <span className="text-emerald-600">Track Your Zakat</span>{" "}
            <span className="text-slate-900">with Confidence</span>
          </h1>

          <p className="mt-6 text-lg leading-8 text-slate-600">
            Simplify your Zakat calculations, track your payments, and ensure
            accuracy in fulfilling your religious obligation. Built with care
            for the modern Muslim.
          </p>

          <div className="mt-10 flex items-center justify-center gap-x-6">
            <Link href="/register">
              <Button size="lg" className="bg-emerald-600 hover:bg-emerald-700">
                Get Started <ArrowRight className="ml-2 h-4 w-4" />
              </Button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Hero;
