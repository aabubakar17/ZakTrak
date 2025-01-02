"use client";

import Link from "next/link";

const Footer = () => {
  return (
    <footer className="bg-slate-50">
      <div className="mx-auto max-w-7xl px-6 py-12 md:flex md:items-center md:justify-between lg:px-8">
        <div className="flex justify-center space-x-6 md:order-2">
          <Link href="/about" className="text-slate-400 hover:text-slate-300">
            About
          </Link>
          <Link href="/privacy" className="text-slate-400 hover:text-slate-300">
            Privacy
          </Link>
          <Link href="/contact" className="text-slate-400 hover:text-slate-300">
            Contact
          </Link>
        </div>
        <div className="mt-8 md:order-1 md:mt-0">
          <p className="text-center text-xs leading-5 text-slate-400">
            &copy; {new Date().getFullYear()} ZakTrak. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
