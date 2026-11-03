const viewContactModal = document.getElementById("view_contact_modal");
const baseUrl = "http://localhost:8080";
// Flowbite modal configuration
const options = {
    placement: "center",
    backdrop: "dynamic",
    backdropClasses:
        "bg-gray-950/50 dark:bg-gray-950/70 fixed inset-0 z-40",
    closable: true,

    onHide: () => {
        console.log("Contact modal hidden");
    },

    onShow: () => {
        console.log("Contact modal shown");
    },

    onToggle: () => {
        console.log("Contact modal toggled");
    }
};

const instanceOptions = {
    id: "view_contact_modal",
    override: true
};

const contactModal = new Modal(
    viewContactModal,
    options,
    instanceOptions
);


// Open modal
function openContactModal() {
    contactModal.show();
}


// Close modal
function closeContactModal() {
    contactModal.hide();
}


// Safely display text
function setText(id, value, fallback = "Not provided") {
    const element = document.getElementById(id);

    if (element) {
        element.textContent =
            value !== null && value !== undefined && String(value).trim() !== ""
                ? String(value)
                : fallback;
    }
}


// Normalize URLs and allow only HTTP/HTTPS
function normalizeUrl(value) {
    if (!value || typeof value !== "string") {
        return null;
    }

    const urlValue = value.trim();

    if (!urlValue) {
        return null;
    }

    try {
        const url = new URL(
            /^[a-zA-Z][a-zA-Z\d+.-]*:/.test(urlValue)
                ? urlValue
                : `https://${urlValue}`
        );

        if (!["http:", "https:"].includes(url.protocol)) {
            return null;
        }

        return url.href;
    } catch {
        return null;
    }
}


// Set an external profile link
function setProfileLink(linkId, textId, value, label) {
    const link = document.getElementById(linkId);
    const text = document.getElementById(textId);
    const url = normalizeUrl(value);

    if (!link || !text) {
        return false;
    }

    if (!url) {
        link.classList.add("hidden");
        link.classList.remove("flex");
        link.removeAttribute("href");
        return false;
    }

    link.href = url;
    text.textContent = value;
    link.classList.remove("hidden");
    link.classList.add("flex");

    return true;
}


// Render extra social links
function renderSocialLinks(links) {
    const section = document.getElementById("social_links_section");
    const container = document.getElementById("contact_social_links");

    container.replaceChildren();

    const validLinks = Array.isArray(links)
        ? links.filter(link => link && typeof link === "object")
        : [];

    let count = 0;

    validLinks.forEach((link, index) => {
        /*
         * Adjust these property names if your SocialLink entity
         * uses different field names.
         */
        const urlValue =
            link.url ??
            link.link ??
            link.socialLink ??
            link.linkUrl;

        const url = normalizeUrl(urlValue);

        if (!url) {
            return;
        }

        const platform =
            link.title ??
            link.platform ??
            link.name ??
            link.type ??
            `Social link ${index + 1}`;

        const card = document.createElement("a");

        card.href = url;
        card.target = "_blank";
        card.rel = "noopener noreferrer";

        card.className =
            "flex min-w-0 items-center gap-3 rounded-xl border " +
            "border-gray-200 p-4 transition hover:border-pink-300 " +
            "hover:bg-pink-50 dark:border-gray-700 " +
            "dark:hover:border-pink-700 dark:hover:bg-gray-800";

        const icon = document.createElement("span");

        icon.className =
            "flex h-10 w-10 shrink-0 items-center justify-center " +
            "rounded-lg bg-pink-100 font-bold text-pink-700 " +
            "dark:bg-pink-950 dark:text-pink-300";

        icon.textContent = String(platform).trim().charAt(0).toUpperCase() || "↗";

        const content = document.createElement("span");
        content.className = "min-w-0 flex-1";

        const title = document.createElement("span");
        title.className =
            "block break-words text-sm font-semibold text-gray-900 " +
            "dark:text-white";
        title.textContent = String(platform);

        const urlText = document.createElement("span");
        urlText.className =
            "mt-1 block break-all text-xs text-indigo-600 " +
            "dark:text-indigo-400";
        urlText.textContent = url;

        content.append(title, urlText);
        card.append(icon, content);
        container.appendChild(card);

        count++;
    });

    section.classList.toggle("hidden", count === 0);
}


// Display the contact profile image
function setContactPicture(picture, name) {
    const image = document.getElementById("contact_picture");

    if (!image) {
        return;
    }

    const imageUrl = normalizeUrl(picture);

    // Support absolute HTTP/HTTPS image URLs.
    // Relative paths such as /uploads/photo.jpg are also allowed.
    const safePicture =
        picture &&
            typeof picture === "string" &&
            picture.trim().startsWith("/") &&
            !picture.trim().startsWith("//")
            ? picture.trim()
            : imageUrl;

    image.onerror = () => {
        image.onerror = null;

        // Use initials when the profile image cannot be loaded.
        const initials = String(name || "?")
            .trim()
            .split(/\s+/)
            .map(part => part.charAt(0))
            .join("")
            .slice(0, 2)
            .toUpperCase();

        image.src =
            "https://ui-avatars.com/api/?name=" +
            encodeURIComponent(initials || "?") +
            "&background=EEF2FF&color=4338CA&size=160";

    };

    image.src = safePicture || (
        "https://ui-avatars.com/api/?name=" +
        encodeURIComponent(name || "?") +
        "&background=EEF2FF&color=4338CA&size=160"
    );

    image.alt = `${name || "Contact"} profile picture`;
}


// Populate all contact information
function populateContactModal(data) {
    setText("contact_name", data.name, "Contact profile");
    setText("profile_name", data.name);
    setText("contact_email", data.email);
    setText("contact_phone", data.phoneNumber);
    setText("contact_address", data.address);
    setText("contact_description", data.description, "No description available.");
    setText("contact_id_badge", data.id, "ID unavailable");

    // Profile image
    setContactPicture(data.picture, data.name);

    // Favorite status
    const isFavorite = data.favorite === true;

    const favoriteBadge = document.getElementById("contact_favorite");

    favoriteBadge.classList.toggle("bg-amber-100", isFavorite);
    favoriteBadge.classList.toggle("text-amber-800", isFavorite);
    favoriteBadge.classList.toggle("dark:bg-amber-950", isFavorite);
    favoriteBadge.classList.toggle("dark:text-amber-300", isFavorite);

    favoriteBadge.classList.toggle("bg-gray-200", !isFavorite);
    favoriteBadge.classList.toggle("text-gray-700", !isFavorite);
    favoriteBadge.classList.toggle("dark:bg-gray-700", !isFavorite);
    favoriteBadge.classList.toggle("dark:text-gray-200", !isFavorite);

    setText(
        "favorite_text",
        isFavorite ? "Favorite contact" : "Not a favorite"
    );

    // Website and LinkedIn
    const hasWebsite = setProfileLink(
        "contact_website",
        "website_text",
        data.websitelink,
        "Visit website"
    );

    const hasLinkedIn = setProfileLink(
        "contact_linkedin",
        "linkedin_text",
        data.linkedInLink,
        "View profile"
    );

    // Show a message if no valid online profiles exist
    document.getElementById("no_online_profiles")
        .classList.toggle("hidden", hasWebsite || hasLinkedIn);

    // Additional social links
    renderSocialLinks(data.links);
}


// Fetch contact details from Spring Boot
async function loadContactData(id) {
    if (id === null || id === undefined || String(id).trim() === "") {
        console.error("Contact ID is required.");
        return;
    }

    try {
        const response = await fetch(
            `/api/contact/${encodeURIComponent(id)}`,
            {
                method: "GET",
                headers: {
                    "Accept": "application/json"
                }
            }
        );

        if (!response.ok) {
            throw new Error(
                `Failed to fetch contact: HTTP ${response.status}`
            );
        }

        const data = await response.json();

        if (!data || typeof data !== "object" || Array.isArray(data)) {
            throw new Error("Invalid contact data received from the server.");
        }

        populateContactModal(data);
        openContactModal();

    } catch (error) {
        console.error("Error loading contact:", error);
        alert("Unable to load contact details. Please try again.");
    }
}


// Close modal when Escape is pressed
document.addEventListener("keydown", event => {
    if (event.key === "Escape" && !viewContactModal.classList.contains("hidden")) {
        closeContactModal();
    }
});


//delete contact

async function deleteContact(id) {
    Swal.fire({
        title: "Do you want to delete the contact?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
        
    }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed){
            const url =    `${baseUrl}/user/contact/delete/` + id;
            window.location.replace(url);
            
        }
        
    });

}