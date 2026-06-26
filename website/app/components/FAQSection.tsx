"use client";

import { motion } from "framer-motion";
import { ChevronDown } from "lucide-react";

export function FAQSection() {
  return (
    <section className="py-24 w-full max-w-4xl mx-auto px-6 relative z-10" aria-labelledby="faq-heading">
      <motion.div 
        initial={{ opacity: 0, y: 30 }}
        whileInView={{ opacity: 1, y: 0 }}
        viewport={{ once: true, margin: "-100px" }}
        className="text-center mb-16 space-y-4"
      >
        <h2 id="faq-heading" className="text-4xl md:text-5xl font-heading font-bold tracking-tight">
          Frequently Asked Questions
        </h2>
        <p className="text-xl opacity-70 max-w-2xl mx-auto font-light">
          Everything you need to know about how The Mute Master works.
        </p>
      </motion.div>
      
      <div className="space-y-4">
        {[
          {
            question: "Will it drain my battery by constantly checking my location?",
            answer: "No. The Mute Master uses Android's native Geofencing APIs which rely on low-power cell towers and Wi-Fi networks to determine location, rather than constantly polling your GPS. This ensures minimal battery impact."
          },
          {
            question: "Does the app need an internet connection to work?",
            answer: "No, all location processing and muting happens 100% locally on your device. We do not collect or upload your location data to any servers, preserving your absolute privacy."
          },
          {
            question: "What happens if a location rule and a time rule overlap?",
            answer: "The app features an intelligent 'Mute Lock'. If multiple rules trigger simultaneously, your phone will remain silenced until all active rules have ended. You'll never be interrupted unexpectedly."
          },
          {
            question: "Can I still hear my media (videos/music) while the ringer is muted?",
            answer: "Yes! You can configure specific volume levels for Media, Alarms, and Ringers within the app. So you can safely watch a video without worrying about an incoming call blasting through your speakers."
          }
        ].map((faq, idx) => (
          <motion.details 
            key={idx}
            initial={{ opacity: 0, y: 10 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: idx * 0.1 }}
            className="group glass-panel rounded-2xl overflow-hidden [&_summary::-webkit-details-marker]:hidden"
          >
            <summary className="flex items-center justify-between p-6 cursor-pointer font-bold text-lg md:text-xl outline-none focus-visible:ring-2 focus-visible:ring-primary">
              <span className="font-heading">{faq.question}</span>
              <ChevronDown className="w-5 h-5 transition-transform group-open:-rotate-180 opacity-70 shrink-0 ml-4" />
            </summary>
            <div className="p-6 pt-0 opacity-70 font-light text-base md:text-lg leading-relaxed">
              {faq.answer}
            </div>
          </motion.details>
        ))}
      </div>
    </section>
  );
}
