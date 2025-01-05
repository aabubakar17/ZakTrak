"use client";
import React from "react";
import { ContainerScroll } from "@/components/ui/container-scroll-animation";
import Image from "next/image";

export function HeroScrollDemo() {
  return (
    <div className="flex flex-col overflow-hidden">
      <ContainerScroll
        titleComponent={
          <>
            <h1 className="text-2xl md:text-5xl font-bold text-black  dark:text-white">
              Simplify Your <br />
              <span className="text-6xl md:text-[10rem] font-bold mt-1 leading-none bg-clip-text text-transparent bg-gradient-to-r from-emerald-600 to-emerald-400">
                Zakat <br /> Journey
              </span>
            </h1>
          </>
        }
      >
        <Image
          src={`/zaktrak-dashboard.png`}
          alt="hero"
          height={720}
          width={1400}
          className="rounded-2xl object-cover h-full object-contain lg:object-right-top"
          draggable={false}
        />
      </ContainerScroll>
    </div>
  );
}
