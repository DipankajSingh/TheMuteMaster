"use client";

import { motion } from "framer-motion";
import { MapPin, Battery, Shield, Clock, VolumeX, CheckCircle } from "lucide-react";
import Image from "next/image";

export function FeaturesBento() {
  return (
    <section className="py-24 md:py-32 w-full max-w-7xl mx-auto px-6 relative z-10">
      <motion.div 
        initial={{ opacity: 0, y: 30 }}
        whileInView={{ opacity: 1, y: 0 }}
        viewport={{ once: true, margin: "-100px" }}
        className="text-center mb-16 md:mb-24 space-y-4"
      >
        <h2 className="text-4xl md:text-5xl font-heading font-bold tracking-tight">
          Intelligent Automation
        </h2>
        <p className="text-xl opacity-70 max-w-3xl mx-auto font-light">
          Designed to run invisibly in the background. Set your rules once, and never think about your ringer volume again.
        </p>
      </motion.div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 md:gap-8">
        {/* Bento Item 1: Geofencing */}
        <motion.div 
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.1 }}
          className="md:col-span-2 glass-panel p-8 md:p-12 rounded-[2.5rem] relative overflow-hidden group min-h-[350px] flex flex-col"
        >
          <div className="absolute top-0 right-0 w-64 h-64 bg-primary/10 rounded-full blur-3xl -translate-y-1/2 translate-x-1/4 group-hover:bg-primary/20 transition-colors duration-500"></div>
          <div className="relative z-10 flex-1">
            <div className="w-16 h-16 rounded-2xl bg-primary/10 text-primary flex items-center justify-center mb-8">
              <MapPin size={32} strokeWidth={1.5} />
            </div>
            <h3 className="text-2xl md:text-3xl font-heading font-bold mb-4">Location-Based Silent Mode</h3>
            <p className="text-lg opacity-70 leading-relaxed font-light max-w-md mb-8">
              Drop a pin on your workplace, university, or local library. The moment you cross the boundary, your phone mutes itself.
            </p>
          </div>
          {/* Abstract visual - Fixed overlap for mobile */}
          <div className="absolute bottom-[-10%] right-[-10%] md:right-[-5%] w-48 h-48 md:w-96 md:h-96 opacity-20 md:opacity-40 group-hover:scale-105 transition-transform duration-700 pointer-events-none -z-1">
            <Image src="/images/mock_bg_location.svg" alt="" fill className="object-contain" />
          </div>
        </motion.div>

        {/* Bento Item 2: Scheduling */}
        <motion.div 
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.2 }}
          className="glass-panel p-8 md:p-12 rounded-[2.5rem] relative overflow-hidden flex flex-col justify-between"
        >
          <div>
            <div className="w-16 h-16 rounded-2xl bg-orange-500/10 text-orange-500 flex items-center justify-center mb-8">
              <Clock size={32} strokeWidth={1.5} />
            </div>
            <h3 className="text-xl md:text-2xl font-heading font-bold mb-4">Advanced Time Schedules</h3>
            <p className="opacity-70 leading-relaxed font-light mb-8">
              Mute your device every weekday from 9 AM to 5 PM, or every night during sleep hours.
            </p>
          </div>
          <div className="flex gap-2 text-sm font-bold text-orange-500 bg-orange-500/10 p-4 rounded-xl w-fit">
            <CheckCircle size={18} /> Repeating Alarms
          </div>
        </motion.div>

        {/* Bento Item 3: Battery */}
        <motion.div 
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.3 }}
          className="glass-panel p-8 md:p-10 rounded-[2.5rem] group"
        >
          <div className="w-14 h-14 rounded-2xl bg-android-green/10 text-android-green-dark dark:text-android-green flex items-center justify-center mb-6">
            <Battery size={28} strokeWidth={1.5} />
          </div>
          <h3 className="text-xl font-heading font-bold mb-3">Battery Efficient</h3>
          <p className="opacity-70 font-light leading-relaxed">
            Utilizes native Android low-power location APIs. It won't drain your battery like standard GPS apps.
          </p>
        </motion.div>

        {/* Bento Item 4: Privacy */}
        <motion.div 
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.4 }}
          className="glass-panel p-8 md:p-10 rounded-[2.5rem] group"
        >
          <div className="w-14 h-14 rounded-2xl bg-blue-500/10 text-blue-500 flex items-center justify-center mb-6">
            <Shield size={28} strokeWidth={1.5} />
          </div>
          <h3 className="text-xl font-heading font-bold mb-3">100% Offline Privacy</h3>
          <p className="opacity-70 font-light leading-relaxed">
            Your location data never leaves your device. No cloud syncing, no accounts required, complete peace of mind.
          </p>
        </motion.div>

        {/* Bento Item 5: Mute Lock */}
        <motion.div 
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ delay: 0.5 }}
          className="glass-panel p-8 md:p-10 rounded-[2.5rem] group bg-gradient-to-br from-background to-primary/5 border-primary/20"
        >
          <div className="w-14 h-14 rounded-2xl bg-primary flex text-white items-center justify-center mb-6 shadow-lg shadow-primary/30">
            <VolumeX size={28} strokeWidth={1.5} />
          </div>
          <h3 className="text-xl font-heading font-bold mb-3">Intelligent Mute Lock</h3>
          <p className="opacity-70 font-light leading-relaxed">
            If a location mute and time schedule overlap, Mute Lock keeps your phone silent until ALL triggers end.
          </p>
        </motion.div>
      </div>
    </section>
  );
}
