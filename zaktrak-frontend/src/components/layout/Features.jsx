"use client";

import { Calculator, LineChart, Wallet, Shield } from "lucide-react";

const features = [
  {
    name: "Precise Calculations",
    description:
      "Accurate Zakat calculations based on current gold prices and your assets, ensuring you fulfill your obligation correctly.",
    icon: Calculator,
  },
  {
    name: "Smart Asset Management",
    description:
      "Easily track all your zakatable assets including cash savings, investments, gold/jewelry, and business assets with our intuitive interface.",
    icon: Wallet,
  },
  {
    name: "Track Progress",
    description:
      "Monitor your Zakat payments and track your progress throughout the year with intuitive visualizations.",
    icon: LineChart,
  },
  {
    name: "Secure & Private",
    description:
      "Your financial information is encrypted and stored securely. We prioritize your privacy and data protection.",
    icon: Shield,
  },
];

const Features = () => {
  return (
    <div className="bg-slate bg-opacity-5 py-16">
      <div className="mx-auto max-w-7xl pb-10 px-6 lg:px-2">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">
            Everything You Need to Manage Your Zakat
          </h2>
          <p className="mt-6 text-lg leading-8 text-slate-600">
            ZakTrak provides all the tools you need to calculate, track, and
            manage your Zakat obligations effectively.
          </p>
        </div>

        {/* Features grid */}
        <div className="mx-auto mt-16 max-w-2xl sm:mt-20 lg:mt-24 lg:max-w-none">
          <dl className="grid max-w-xl grid-cols-1 gap-x-8 gap-y-16 lg:max-w-none lg:grid-cols-4">
            {features.map((feature) => (
              <div key={feature.name} className="flex flex-col">
                <dt className="flex items-center gap-x-3 text-lg font-semibold leading-7 text-slate-900">
                  <feature.icon
                    className="h-6 w-6 flex-none text-emerald-600"
                    aria-hidden="true"
                  />
                  {feature.name}
                </dt>
                <dd className="mt-4 flex flex-auto flex-col text-base leading-7 text-slate-600">
                  <p className="flex-aut">{feature.description}</p>
                </dd>
              </div>
            ))}
          </dl>
        </div>
      </div>
    </div>
  );
};

export default Features;
