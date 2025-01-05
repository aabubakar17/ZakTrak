"use client";

import Link from "next/link";
import { Github, Linkedin } from "lucide-react";

const Footer = () => {
  return (
    <footer className="bg-slate-50">
      <div className="mx-auto max-w-8xl px-6 py-12 md:flex md:items-center md:justify-between lg:px-8">
        <div className="flex justify-center space-x-6 md:order-2">
          <a
            href="https://github.com/aabubakar17"
            target="_blank"
            rel="noopener noreferrer"
            className="text-slate-400 hover:text-emerald-600"
          >
            <Github className="h-6 w-6" />
          </a>
          <a
            href="https://www.linkedin.com/in/abubakar-abubakar-46a9141a1/"
            target="_blank"
            rel="noopener noreferrer"
            className="text-slate-400 hover:text-emerald-600"
          >
            <Linkedin className="h-6 w-6" />
          </a>
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
