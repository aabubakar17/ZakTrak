"use client";

import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import { ExternalLink } from "lucide-react";

const charities = [
  {
    name: "Islamic Relief",
    description:
      "International humanitarian organization providing global relief and development",
    logo: "/islamic-relief-logo.png",
    website: "https://islamic-relief.org/zakat/",
    backgroundColor: "bg-green-50",
  },
  {
    name: "MATW Project",
    description:
      "Focused on delivering life-saving aid, reaching over 19 million Muslims",
    logo: "/charity-right-logo.png",
    website: "https://charityright.org.uk/zakat/",
    backgroundColor: "bg-orange-50",
  },

  {
    name: "National Zakat Foundation",
    description:
      "Dedicated to collecting and distributing Zakat locally in the UK",
    logo: "/nzf-logo.png",
    website: "https://nzf.org.uk/pay-zakat/",
    backgroundColor: "bg-purple-50",
  },

  {
    name: "Muslim Aid",
    description:
      "UK-based charity focusing on emergency relief and sustainable development",
    logo: "/muslim-aid-logo.png",
    website: "https://www.muslimaid.org/appeals/zakat/",
    backgroundColor: "bg-blue-50",
  },
];

export default function ZakatCharities() {
  return (
    <Card className="p-6 mb-8">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h2 className="text-lg font-semibold">Pay Your Zakat</h2>
          <p className="text-sm text-slate-600">
            Choose from trusted Zakat collection organizations
          </p>
        </div>
      </div>

      <Carousel className="w-full">
        <CarouselContent>
          {charities.map((charity) => (
            <CarouselItem
              key={charity.name}
              className="md:basis-1/2 lg:basis-1/3"
            >
              <Card className={`${charity.backgroundColor} border-none h-full`}>
                <div className="p-6 flex flex-col h-full">
                  <div className="flex-1 space-y-4">
                    <div className="h-20 flex items-center justify-center">
                      <div className="text-lg font-bold">{charity.name}</div>
                    </div>
                    <p className="text-sm text-slate-600">
                      {charity.description}
                    </p>
                  </div>
                  <Button
                    className="w-full mt-4"
                    onClick={() => window.open(charity.website, "_blank")}
                  >
                    Pay Zakat
                    <ExternalLink className="ml-2 h-4 w-4" />
                  </Button>
                </div>
              </Card>
            </CarouselItem>
          ))}
        </CarouselContent>
        <CarouselPrevious />
        <CarouselNext />
      </Carousel>
    </Card>
  );
}
