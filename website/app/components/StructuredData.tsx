
export default function StructuredData() {
  const schemaData = {
    "@context": "https://schema.org",
    "@type": "SoftwareApplication",
    name: "The Mute Master",
    description:
      "An elegant Android app that automatically silences your phone in quiet zones or on a custom schedule using smart automation.",
    url: "https://dipankajsingh.github.io/MuteMaster/",
    applicationCategory: "Productivity",
    offers: {
      "@type": "Offer",
      price: "0",
      priceCurrency: "USD",
    },
    aggregateRating: {
      "@type": "AggregateRating",
      ratingValue: "4.8",
      ratingCount: "1200",
    },
    operatingSystem: "Android",
  };

  return (
    <script
      type="application/ld+json"
      dangerouslySetInnerHTML={{ __html: JSON.stringify(schemaData) }}
    />
  );
}
