import Hero from "@/components/layout/Hero";
import Features from "@/components/layout/Features";

export default function Home() {
  return (
    <>
      <div className="relative isolate overflow-hidden">
        {/* Background gradient */}
        <div className="absolute inset-x-0 top-0 h-[70vh] bg-gradient-to-b from-emerald-50 via-emerald-50/50 to-transparent" />

        <Hero />
        <Features />
      </div>
    </>
  );
}
