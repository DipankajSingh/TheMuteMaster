/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        './app/**/*.{js,ts,jsx,tsx}',
        './pages/**/*.{js,ts,jsx,tsx}',
        './components/**/*.{js,ts,jsx,tsx}',
    ],
    theme: {
        extend: {
            colors: {
                'android-green': 'var(--android-green)',
                'android-green-dark': 'var(--android-green-dark)',
            },
        },
    },
    plugins: [],
};
