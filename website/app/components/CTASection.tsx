"use client";

import { motion } from "framer-motion";
import { Smartphone } from "lucide-react";
import { SiGoogleplay } from "react-icons/si";

export function CTASection() {
  return (
    <section className="relative py-24 md:py-32 mt-auto text-black overflow-hidden isolate">
      <div className="absolute inset-0 bg-gradient-to-br from-primary to-orange-400 z-[-2]"></div>
      
      {/* Background graphic hidden on mobile to prevent text overlap unreadability */}
      <div className="hidden md:block absolute top-0 right-0 w-full h-full bg-[url('/images/mock_bg_location.svg')] opacity-10 bg-cover bg-center z-[-1]"></div>
      
      <motion.div 
        initial={{ scale: 0.95, opacity: 0 }}
        whileInView={{ scale: 1, opacity: 1 }}
        viewport={{ once: true }}
        transition={{ duration: 0.6 }}
        className="max-w-4xl mx-auto px-6 text-center space-y-10 relative z-10"
      >
        <Smartphone size={64} className="mx-auto text-white/90 drop-shadow-md" />
        <h2 className="text-4xl md:text-6xl lg:text-7xl font-heading font-extrabold tracking-tighter text-white drop-shadow-lg">
          Ready for a quieter life?
        </h2>
        <p className="text-xl md:text-2xl text-white font-light max-w-2xl mx-auto drop-shadow-md">
          Join thousands of users who have automated their device's ringer. Download The Mute Master for free today.
        </p>
        
        <div className="pt-6">
          <a
            href="https://play.google.com/store/apps/details?id=com.dipdev.themutemaster&pcampaignid=web_share"
            target="_blank"
            rel="noopener noreferrer"
            className="inline-flex items-center justify-center gap-3 bg-white text-black font-bold text-lg md:text-xl px-10 py-5 rounded-full shadow-2xl hover:scale-105 transition-all hover:shadow-[0_20px_50px_rgba(0,0,0,0.3)] active:scale-95"
          >
            <SiGoogleplay className="w-6 h-6 md:w-7 md:h-7" />
            Download Free on Google Play
          </a>
        </div>
        <p className="text-white/80 text-xs md:text-sm pt-8 font-medium tracking-widest uppercase drop-shadow-sm">
          No Ads • No Subscriptions • Locally Processed
        </p>
      </motion.div>
    </section>
  );
}
