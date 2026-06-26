"use client";

import { motion } from "framer-motion";
import { CheckCircle } from "lucide-react";
import Image from "next/image";

export function UIShowcase() {
  return (
    <section className="py-24 md:py-40 relative w-full overflow-hidden">
      <div className="absolute inset-0 bg-foreground/[0.02] skew-y-3 transform origin-top-left -z-10"></div>
      
      <div className="max-w-7xl mx-auto px-6 flex flex-col lg:flex-row items-center gap-16 lg:gap-24">
        <motion.div 
          initial={{ opacity: 0, x: -50 }}
          whileInView={{ opacity: 1, x: 0 }}
          viewport={{ once: true, margin: "-100px" }}
          transition={{ duration: 0.8 }}
          className="flex-1 space-y-8 z-10"
        >
          <h2 className="text-4xl md:text-6xl font-heading font-bold tracking-tighter leading-[1.1]">
            Beautiful Interface.<br/>
            <span className="opacity-50">Powerful Engine.</span>
          </h2>
          <p className="text-xl opacity-70 leading-relaxed font-light">
            We ditched the complex settings menus. The Mute Master offers a clean, modern UI that lets you manage your quiet zones and schedules in seconds. 
          </p>
          
          <ul className="space-y-6 pt-6">
            {[
              { title: "Material You Support", desc: "Adapts to your system's dynamic colors." },
              { title: "Custom Media Volume", desc: "Mute the ringer, but keep media volume at 30%." },
              { title: "Dark Mode Ready", desc: "Easy on the eyes, perfect for night-time scheduling." }
            ].map((feature, idx) => (
              <li key={idx} className="flex gap-4 items-start">
                <div className="w-8 h-8 rounded-full bg-foreground/10 flex items-center justify-center shrink-0 mt-1">
                  <CheckCircle size={16} className="opacity-70" />
                </div>
                <div>
                  <h4 className="text-lg font-heading font-bold">{feature.title}</h4>
                  <p className="opacity-70 text-sm md:text-base">{feature.desc}</p>
                </div>
              </li>
            ))}
          </ul>
        </motion.div>

        {/* Parallax Phones - Fixed Mobile Layout */}
        <div className="flex-1 relative w-full flex flex-col md:flex-row justify-center items-center gap-8 md:gap-6 h-auto md:h-[600px] perspective-[1000px] mt-8 md:mt-0">
          {/* Phone Mockup 1 - Floating Up */}
          <motion.div 
            initial={{ y: 50, rotateY: 10, opacity: 0 }}
            whileInView={{ y: -10, rotateY: 15, opacity: 1 }}
            viewport={{ once: true, margin: "-100px" }}
            transition={{ duration: 1, ease: "easeOut" }}
            className="relative w-40 md:w-64 aspect-[9/19] rounded-[2rem] md:rounded-[2.5rem] overflow-hidden shadow-2xl border-[6px] md:border-8 border-foreground/10 bg-background z-20"
          >
            <Image
              src="/images/mutemaster_03_saved_zones.png"
              alt="Saved Zones"
              fill
              sizes="(max-width: 768px) 160px, 256px"
              className="object-cover dark:opacity-90 dark:brightness-90"
            />
          </motion.div>

          {/* Phone Mockup 2 - Floating Down */}
          <motion.div 
            initial={{ y: -50, rotateY: -10, opacity: 0 }}
            whileInView={{ y: 30, rotateY: -15, opacity: 1 }}
            viewport={{ once: true, margin: "-100px" }}
            transition={{ duration: 1, ease: "easeOut", delay: 0.2 }}
            className="relative w-40 md:w-64 aspect-[9/19] rounded-[2rem] md:rounded-[2.5rem] overflow-hidden shadow-2xl border-[6px] md:border-8 border-foreground/10 bg-background z-10 md:-ml-24 mt-8 md:mt-24"
          >
            <Image
              src="/images/mutemaster_04_schedules.png"
              alt="Schedules"
              fill
              sizes="(max-width: 768px) 160px, 256px"
              className="object-cover dark:opacity-90 dark:brightness-90"
            />
          </motion.div>
        </div>
      </div>
    </section>
  );
}
