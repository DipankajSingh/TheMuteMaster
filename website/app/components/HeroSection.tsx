"use client";

import { motion, useScroll, useTransform } from "framer-motion";
import { FaAndroid } from "react-icons/fa";
import { SiGoogleplay } from "react-icons/si";

export function HeroSection() {
  const { scrollYProgress } = useScroll();
  const heroY = useTransform(scrollYProgress, [0, 0.5], [0, 200]);
  const opacityTransform = useTransform(scrollYProgress, [0, 0.2], [1, 0]);

  return (
    <section className="relative w-full max-w-7xl mx-auto px-6 pt-24 pb-20 md:pt-32 md:pb-32 flex flex-col items-center text-center">
      <motion.div 
        style={{ y: heroY, opacity: opacityTransform }}
        className="flex flex-col items-center z-10"
      >
        <motion.div 
          initial={{ scale: 0.8, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="inline-flex items-center gap-2 px-4 py-2 rounded-full border border-android-green text-xs md:text-sm font-bold text-android-green-dark dark:text-android-green uppercase tracking-widest mb-6 bg-android-green/10 backdrop-blur-md"
        >
          <FaAndroid className="w-4 h-4 md:w-5 md:h-5" />
          Designed for Android
        </motion.div>
        
        <motion.h2 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.7, delay: 0.3 }}
          className="text-5xl sm:text-6xl md:text-8xl font-heading font-extrabold tracking-tighter leading-[1.1] mb-6 max-w-5xl"
        >
          Silence the noise.<br />
          <span className="text-transparent bg-clip-text bg-gradient-to-r from-primary to-orange-400 drop-shadow-sm">
            Reclaim your focus.
          </span>
        </motion.h2>
        
        <motion.p 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.7, delay: 0.4 }}
          className="text-lg md:text-2xl max-w-2xl opacity-70 leading-relaxed font-light mb-10 px-4"
        >
          The ultimate location-based Do Not Disturb app. Walk into the office, step into class, or go to sleep—your phone mutes itself instantly.
        </motion.p>

        <motion.div 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.7, delay: 0.5 }}
          className="flex flex-col sm:flex-row gap-6 w-full justify-center px-4"
        >
          <a
            href="https://play.google.com/store/apps/details?id=com.dipdev.themutemaster&pcampaignid=web_share"
            target="_blank"
            rel="noopener noreferrer"
            className="group relative inline-flex items-center justify-center gap-3 bg-primary text-white font-semibold text-lg md:text-xl px-8 py-4 md:px-10 md:py-5 rounded-full shadow-[0_8px_30px_-6px_rgba(240,81,35,0.6)] hover:bg-primary-container transition-all transform hover:-translate-y-1 active:scale-95 overflow-hidden w-full sm:w-auto"
          >
            <div className="absolute inset-0 w-full h-full bg-white/20 -skew-x-12 -translate-x-full group-hover:animate-[shimmer_1.5s_infinite]"></div>
            <SiGoogleplay className="w-6 h-6 relative z-10" />
            <span className="relative z-10">Get it on Google Play</span>
          </a>
        </motion.div>
      </motion.div>
    </section>
  );
}
