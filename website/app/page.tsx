"use client";

import Image from "next/image";
import Link from "next/link";
import { MapPin, Battery, Shield, Clock } from "lucide-react";
import { FaAndroid } from "react-icons/fa";
import { SiGoogleplay } from "react-icons/si";
import { ThemeToggle } from "./components/ThemeToggle";

export default function Home() {
  return (
    <div className="flex flex-col min-h-screen font-sans selection:bg-primary/30">
      {/* Header */}
      <header className="px-6 py-6 flex justify-between items-center w-full max-w-7xl mx-auto z-50">
        <div className="flex items-center gap-4">
          <Image
            src="/images/appicon.png"
            alt="The Mute Master Logo"
            width={42}
            height={42}
            className="rounded-xl shadow-md border border-foreground/10"
          />
          <h1 className="text-xl font-bold tracking-tight">The Mute Master</h1>
        </div>
        <nav className="flex items-center gap-6">
          <Link
            href="/privacy"
            className="text-sm font-medium opacity-70 hover:opacity-100 hover:text-primary transition-colors"
          >
            Privacy Policy
          </Link>
          <Link
            href="/terms"
            className="text-sm font-medium opacity-70 hover:opacity-100 hover:text-primary transition-colors"
          >
            Terms
          </Link>
          <ThemeToggle />
        </nav>
      </header>

      {/* Hero Section */}
      <section className="relative w-full max-w-7xl mx-auto px-6 py-20 md:py-32 flex flex-col md:flex-row items-center gap-12 overflow-hidden">
        {/* Text Content */}
        <div className="flex-1 flex flex-col items-center md:items-start text-center md:text-left space-y-8 z-10">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full border border-green-700 text-xs font-bold text-green-700 uppercase tracking-widest mb-2 bg-green-100">
            <FaAndroid className="w-4 h-4" />
            Free on Android
          </div>
          <h2 className="text-5xl md:text-7xl font-extrabold tracking-tight leading-loose md:leading-[1.15]">
            Silence Your Phone. <br />
            <span className="text-transparent bg-clip-text bg-linear-to-r from-primary to-orange-400">
              Automatically.
            </span>
          </h2>
          <p className="text-lg md:text-xl max-w-lg opacity-70 leading-relaxed font-light">
            Never worry about your phone ringing loudly at work, school, or during sleep
            again. Pick your quiet zones and schedules, and we will handle the rest.
          </p>

          <div className="pt-6 flex flex-col sm:flex-row gap-6 w-full justify-center md:justify-start">
            <a
              href="https://play.google.com/store/apps/details?id=com.dipdev.themutemaster&pcampaignid=web_share"
              target="_blank"
              rel="noopener noreferrer"
              className="group relative inline-flex items-center justify-center gap-2 bg-primary text-white font-semibold text-lg px-8 py-4 rounded-full shadow-[0_8px_20px_-6px_rgba(240,81,35,0.5)] hover:bg-primary-container transition-all transform hover:-translate-y-1 active:scale-95"
            >
              <SiGoogleplay className="w-5 h-5" />
              <span>Get it on Google Play</span>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-5 w-5 group-hover:translate-x-1 transition-transform"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fillRule="evenodd"
                  d="M10.293 3.293a1 1 0 011.414 0l6 6a1 1 0 010 1.414l-6 6a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-4.293-4.293a1 1 0 010-1.414z"
                  clipRule="evenodd"
                />
              </svg>
            </a>
          </div>
        </div>

        {/* Visual Content (Screenshots) */}
        <div className="flex-1 relative w-full flex justify-center z-10 mt-16 md:mt-0">
          <div className="relative w-72 md:w-[320px] aspect-9/19 rounded-[2.5rem] overflow-hidden border-8 border-foreground/5 bg-background shadow-2xl hover:scale-[1.02] transition-transform duration-500">
            <Image
              src="/images/screenshot-1.png"
              alt="The Mute Master App Screenshot"
              fill
              sizes="(max-width: 768px) 100vw, 320px"
              className="object-cover"
              priority
            />
          </div>
          {/* Decorative Glow */}
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full h-[80%] bg-primary/10 blur-[100px] rounded-full z-0 pointer-events-none" />
        </div>
      </section>

      {/* Easy to Understand Features Grid */}
      <section className="py-24 w-full bg-foreground/5">
        <div className="max-w-7xl mx-auto px-6 relative z-10">
          <div className="text-center mb-16 space-y-4">
            <h2 className="text-4xl font-bold tracking-tight">
              Smart, Simple, and Reliable
            </h2>
            <p className="text-lg opacity-70 max-w-2xl mx-auto">
              Built from the ground up to respect your battery and run
              invisibly.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 text-center md:text-left">
            {/* Feature 1 */}
            <div className="glass-panel p-8 rounded-4xl transition-all duration-300 transform hover:-translate-y-2">
              <div className="icon-container w-14 h-14 flex items-center justify-center rounded-2xl mb-6 mx-auto md:mx-0">
                <MapPin size={28} strokeWidth={1.5} />
              </div>
              <h3 className="text-2xl font-bold mb-3">Geofencing & Quiet Zones</h3>
              <p className="opacity-70 leading-relaxed font-light">
                Simply drop a pin on the map to create a geofenced area. Your phone triggers Auto-muting instantly the moment you enter your selected location, keeping distractions at bay.
              </p>
            </div>

            {/* Feature 2: NEW */}
            <div className="glass-panel p-8 rounded-4xl transition-all duration-300 transform hover:-translate-y-2">
              <div className="icon-container w-14 h-14 flex items-center justify-center rounded-2xl mb-6 mx-auto md:mx-0">
                <Clock size={28} strokeWidth={1.5} />
              </div>
              <h3 className="text-2xl font-bold mb-3">Advanced Scheduling</h3>
              <p className="opacity-70 leading-relaxed font-light">
                Set it and forget it. Automate your silence during work hours, classes, or sleep with custom repeating schedules. Auto-muting ensures you are never disturbed.
              </p>
            </div>

            {/* Feature 3 */}
            <div className="glass-panel p-8 rounded-4xl transition-all duration-300 transform hover:-translate-y-2">
              <div className="icon-container w-14 h-14 flex items-center justify-center rounded-2xl mb-6 mx-auto md:mx-0">
                <Battery size={28} strokeWidth={1.5} />
              </div>
              <h3 className="text-2xl font-bold mb-3">Battery Efficient</h3>
              <p className="opacity-70 leading-relaxed font-light">
                Optimized for Android. We use highly optimized, low-power geofencing technology to ensure your battery easily lasts through the entire day while still Auto-muting accurately.
              </p>
            </div>

            {/* Feature 4 */}
            <div className="glass-panel p-8 rounded-4xl transition-all duration-300 transform hover:-translate-y-2">
              <div className="icon-container w-14 h-14 flex items-center justify-center rounded-2xl mb-6 mx-auto md:mx-0">
                <Shield size={28} strokeWidth={1.5} />
              </div>
              <h3 className="text-2xl font-bold mb-3">Privacy First</h3>
              <p className="opacity-70 leading-relaxed font-light">
                No tracking. No cloud. Your saved locations and schedules stay
                encrypted locally on your personal device.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Intelligent Mute Lock Section */}
      <section className="py-24 w-full bg-primary/5">
        <div className="max-w-7xl mx-auto px-6 flex flex-col md:flex-row items-center gap-16">
          <div className="flex-1 order-2 md:order-1 flex justify-center relative w-full">
            <div className="relative w-64 md:w-75 aspect-9/19 shadow-2xl rounded-[2.5rem] overflow-hidden border-8 border-foreground/10 bg-background">
              <Image
                src="/images/screenshot-1.png"
                alt="App Screenshot"
                fill
                sizes="(max-width: 768px) 100vw, 300px"
                className="object-cover"
              />
            </div>
            {/* Overlay graphic indicating "Multiple Triggers" */}
            <div className="absolute -right-4 top-1/4 bg-background p-4 rounded-2xl shadow-xl border border-foreground/10 z-20 animate-bounce-slow">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-blue-500/10 flex items-center justify-center">
                  <MapPin size={20} className="text-blue-500" />
                </div>
                <div>
                  <p className="text-xs font-bold uppercase tracking-wider opacity-50">Active Zone</p>
                  <p className="text-sm font-bold">Office</p>
                </div>
              </div>
            </div>
            <div className="absolute -left-4 bottom-1/4 bg-background p-4 rounded-2xl shadow-xl border border-foreground/10 z-20 animate-bounce-slow delay-150">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-orange-500/10 flex items-center justify-center">
                  <Clock size={20} className="text-orange-500" />
                </div>
                <div>
                  <p className="text-xs font-bold uppercase tracking-wider opacity-50">Active Schedule</p>
                  <p className="text-sm font-bold">Focus Time</p>
                </div>
              </div>
            </div>
          </div>
          <div className="flex-1 order-1 md:order-2 space-y-8">
            <h2 className="text-4xl font-bold tracking-tight">
              Intelligent Mute Lock
            </h2>
            <p className="text-lg opacity-70 leading-relaxed font-light">
              What if you&apos;re in a silent zone AND a scheduled mute starts? Our 
              proprietary <strong>Mute Lock</strong> system ensures your phone 
              stays silent until <em>all</em> active triggers are cleared.
            </p>
            <ul className="space-y-4">
              <li className="flex gap-3 items-center">
                <div className="w-6 h-6 rounded-full bg-green-500/20 flex items-center justify-center">
                  <svg className="w-4 h-4 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M5 13l4 4L19 7" /></svg>
                </div>
                <span className="opacity-80">Overlapping triggers handled automatically</span>
              </li>
              <li className="flex gap-3 items-center">
                <div className="w-6 h-6 rounded-full bg-green-500/20 flex items-center justify-center">
                  <svg className="w-4 h-4 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M5 13l4 4L19 7" /></svg>
                </div>
                <span className="opacity-80">Custom media volume for each schedule</span>
              </li>
              <li className="flex gap-3 items-center">
                <div className="w-6 h-6 rounded-full bg-green-500/20 flex items-center justify-center">
                  <svg className="w-4 h-4 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M5 13l4 4L19 7" /></svg>
                </div>
                <span className="opacity-80">Smart restoration of previous audio state</span>
              </li>
            </ul>
          </div>
        </div>
      </section>

      {/* How it Works / Screenshots */}
      <section className="py-24 max-w-7xl mx-auto px-6 w-full">
        <div className="flex flex-col md:flex-row-reverse w-full items-center gap-16">
          <div className="flex-1 space-y-8">
            <h2 className="text-4xl font-bold tracking-tight">
              Total Control, Zero Effort.
            </h2>
            <div className="space-y-6">
              <div className="flex gap-4 items-start">
                <div className="shrink-0 w-8 h-8 rounded-full bg-primary/10 text-primary flex items-center justify-center font-bold">
                  1
                </div>
                <div>
                  <h4 className="text-xl font-bold mb-1">Pick Your Triggers</h4>
                  <p className="opacity-70">
                    Choose a location on the map or set a repeating time window
                    for when you need silence.
                  </p>
                </div>
              </div>
              <div className="flex gap-4 items-start">
                <div className="shrink-0 w-8 h-8 rounded-full bg-primary/10 text-primary flex items-center justify-center font-bold">
                  2
                </div>
                <div>
                  <h4 className="text-xl font-bold mb-1">Customize the Sound</h4>
                  <p className="opacity-70">
                    Set per-trigger rules. Mute everything, or just the ringer 
                    while keeping your media volume at exactly 20%.
                  </p>
                </div>
              </div>
              <div className="flex gap-4 items-start">
                <div className="shrink-0 w-8 h-8 rounded-full bg-primary text-white shadow-[0_4px_10px_rgba(240,81,35,0.4)] flex items-center justify-center font-bold">
                  3
                </div>
                <div>
                  <h4 className="text-xl font-bold mb-1">Live Your Life</h4>
                  <p className="opacity-70">
                    Your phone intelligently manages its audio state so you 
                    never have to fumble with volume buttons again.
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div className="flex-1 flex justify-center relative w-full">
            <div className="relative w-64 md:w-75 aspect-9/19 shadow-2xl rounded-[2.5rem] overflow-hidden border-8 border-foreground/10 bg-background">
              <Image
                src="/images/screenshot-4.png"
                alt="Adding a new location"
                fill
                sizes="(max-width: 768px) 100vw, 300px"
                className="object-cover"
              />
            </div>
          </div>
        </div>
      </section>

      {/* Final CTA */}
      <section className="bg-primary py-24 mt-auto rounded-t-[3rem] text-black">
        <div className="max-w-4xl mx-auto px-6 text-center space-y-8">
          <h2 className="text-4xl md:text-5xl font-extrabold tracking-tight">
            Ready to silence the distractions?
          </h2>
          <p className="text-xl opacity-90 pb-4">
            Download The Mute Master for free today to streamline your life!
          </p>
          <a
            href="https://play.google.com/store/apps/details?id=com.dipdev.themutemaster&pcampaignid=web_share"
            target="_blank"
            rel="noopener noreferrer"
            className="inline-flex items-center justify-center gap-3 bg-white text-black font-bold text-xl px-10 py-5 rounded-full shadow-xl hover:scale-105 transition-transform"
          >
            <SiGoogleplay className="w-6 h-6" />
            Download from Google Play
          </a>
        </div>
      </section>

      {/* Footer */}
      <footer className="w-full py-10 bg-primary border-t border-white/20 text-center flex flex-col items-center gap-4 text-black">
        <div className="flex gap-6 mb-2">
          <Link
            href="/privacy"
            className="opacity-80 hover:opacity-100 transition-opacity font-medium"
          >
            Privacy Policy
          </Link>
          <Link
            href="/terms"
            className="opacity-80 hover:opacity-100 transition-opacity font-medium"
          >
            Terms & Conditions
          </Link>
          <a
            href="mailto:dipankajsingh25@gmail.com"
            className="opacity-80 hover:opacity-100 transition-opacity font-medium"
          >
            Support
          </a>
        </div>
        <p className="text-sm opacity-70">
          © {new Date().getFullYear()} Dipdev | The Mute Master. Built for
          Android.
        </p>
      </footer>
    </div>
  );
}
