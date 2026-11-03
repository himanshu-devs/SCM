
let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
});

function changeTheme() {
    const html = document.documentElement;

    // Apply the saved theme
    html.classList.toggle("dark", currentTheme === "dark");

    // Get both theme buttons
    const themeButtons = [
        document.querySelector("#theme_change_button"),
        document.querySelector("#theme_change_button_mobile")
    ].filter(Boolean);

    // Update button labels
    function updateThemeLabels() {
        themeButtons.forEach(button => {
            const label = button.querySelector("span");

            if (label) {
                label.textContent =
                    currentTheme === "dark" ? "Light" : "Dark";
            }
        });
    }

    updateThemeLabels();

    // Attach the same functionality to both buttons
    themeButtons.forEach(button => {
        button.addEventListener("click", () => {
            currentTheme = currentTheme === "dark" ? "light" : "dark";

            html.classList.toggle("dark", currentTheme === "dark");

            setTheme(currentTheme);
            updateThemeLabels();
        });
    });
}

// Save theme to localStorage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

// Get saved theme from localStorage
function getTheme() {
    return localStorage.getItem("theme") || "light";
}
