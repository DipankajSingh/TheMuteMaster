"use client";

import Image from "next/image";
import Link from "next/link";
import { useState, useEffect } from "react";
import { useTheme } from "next-themes";
import { motion } from "framer-motion";

import { ThemeToggle } from "./components/ThemeToggle";
import { HeroSection } from "./components/HeroSection";
import { FeaturesBento } from "./components/FeaturesBento";
import { UIShowcase } from "./components/UIShowcase";
import { FAQSection } from "./components/FAQSection";
import { CTASection } from "./components/CTASection";

export default function Home() {
  const { resolvedTheme } = useTheme();
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  const isDark = mounted && resolvedTheme === "dark";

  return (
    <div className="flex flex-col min-h-screen font-sans selection:bg-primary/30 overflow-hidden relative">
      {/* Background Animated Blobs */}
      <div className="fixed inset-0 overflow-hidden pointer-events-none z-[-1]">
        <div className="absolute -top-[20%] -left-[10%] w-[70vw] h-[70vw] md:w-[50vw] md:h-[50vw] rounded-full bg-primary/10 mix-blend-multiply blur-[100px] md:blur-[120px] animate-blob dark:mix-blend-screen opacity-60"></div>
        <div className="absolute top-[20%] -right-[10%] w-[60vw] h-[60vw] md:w-[40vw] md:h-[40vw] rounded-full bg-android-green/10 mix-blend-multiply blur-[100px] md:blur-[120px] animate-blob animation-delay-2000 dark:mix-blend-screen opacity-60"></div>
        <div className="absolute -bottom-[20%] left-[10%] md:left-[20%] w-[80vw] h-[80vw] md:w-[60vw] md:h-[60vw] rounded-full bg-orange-500/10 mix-blend-multiply blur-[100px] md:blur-[120px] animate-blob animation-delay-4000 dark:mix-blend-screen opacity-50"></div>
      </div>

      {/* Header */}
      <motion.header 
        initial={{ y: -50, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ duration: 0.8, ease: "easeOut" }}
        className="px-6 py-4 md:py-6 flex justify-between items-center w-full max-w-7xl mx-auto z-50 sticky top-0 backdrop-blur-lg bg-background/50 border-b border-foreground/5 rounded-b-3xl shadow-sm"
      >
        <div className="flex items-center gap-3 md:gap-4">
          <Image
            src="/images/appicon.png"
            alt="The Mute Master Logo"
            width={36}
            height={36}
            className="rounded-xl shadow-md border border-foreground/10 hover:scale-105 transition-transform md:w-[42px] md:h-[42px]"
          />
          <h1 className="text-lg md:text-xl font-heading font-bold tracking-tight">The Mute Master</h1>
        </div>
        <nav className="flex items-center gap-4 md:gap-6">
          <Link
            href="/privacy"
            className="hidden md:block text-sm font-medium opacity-70 hover:opacity-100 hover:text-primary transition-colors"
          >
            Privacy
          </Link>
          <Link
            href="/terms"
            className="hidden md:block text-sm font-medium opacity-70 hover:opacity-100 hover:text-primary transition-colors"
          >
            Terms
          </Link>
          <ThemeToggle />
        </nav>
      </motion.header>

      <HeroSection />

      {/* Feature Graphic Floating (placed globally underneath Hero) */}
      <section className="relative w-full max-w-7xl mx-auto px-6 pb-12 flex justify-center">
        <motion.div 
          initial={{ y: 100, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 1, delay: 0.6, type: "spring", bounce: 0.4 }}
          className="relative w-full max-w-5xl aspect-[1024/500] rounded-[2rem] md:rounded-[3rem] overflow-hidden shadow-2xl border-[6px] md:border-8 border-foreground/10 bg-background/50 backdrop-blur-sm z-20 group"
        >
          <div className="absolute inset-0 bg-gradient-to-t from-background/80 to-transparent z-10 pointer-events-none rounded-[2rem] md:rounded-[3rem]"></div>
          <Image
            src="/images/mutemaster_feature_graphic_light_hq.png"
            alt="The Mute Master Dashboard Showcase"
            fill
            sizes="(max-width: 1024px) 100vw, 1024px"
            className="object-cover group-hover:scale-105 transition-transform duration-1000 ease-out dark:opacity-90 dark:brightness-90"
            priority
          />
        </motion.div>
      </section>

      <FeaturesBento />
      <UIShowcase />
      <FAQSection />
      <CTASection />

      {/* Footer */}
      <footer className="w-full py-12 px-6 bg-background border-t border-foreground/10 text-center flex flex-col items-center gap-6">
        <Image
            src="/images/appicon.png"
            alt="The Mute Master Logo"
            width={48}
            height={48}
            className="rounded-2xl grayscale opacity-50 hover:grayscale-0 hover:opacity-100 transition-all duration-300"
          />
        <div className="flex flex-wrap justify-center gap-6 md:gap-8 mb-2">
          <Link
            href="/privacy"
            className="opacity-60 hover:opacity-100 hover:text-primary transition-all font-medium text-sm md:text-base"
          >
            Privacy Policy
          </Link>
          <Link
            href="/terms"
            className="opacity-60 hover:opacity-100 hover:text-primary transition-all font-medium text-sm md:text-base"
          >
            Terms & Conditions
          </Link>
          <a
            href="mailto:dipankajsingh25@gmail.com"
            className="opacity-60 hover:opacity-100 hover:text-primary transition-all font-medium text-sm md:text-base"
          >
            Support
          </a>
        </div>
        <p className="text-xs md:text-sm opacity-50 max-w-sm mx-auto">
          © {new Date().getFullYear()} Dipdev | The Mute Master. Built for Android.
        </p>
      </footer>
    </div>
  );
}
