
document.addEventListener("DOMContentLoaded", () => {
    const contactImage = document.getElementById("contactImage");
    const fileName = document.getElementById("fileName");
    const imagePreview = document.getElementById("upload_image_prview");

    if (!contactImage || !fileName || !imagePreview) {
        console.error("Contact image preview elements not found.");
        return;
    }

    let previewUrl = null;

    contactImage.addEventListener("change", () => {
        const file = contactImage.files[0];

        // Clear the previous preview URL
        if (previewUrl) {
            URL.revokeObjectURL(previewUrl);
            previewUrl = null;
        }

        if (!file) {
            fileName.textContent = "PNG, JPG or WEBP";
            imagePreview.src = "";
            imagePreview.classList.add("hidden");
            return;
        }

        // Validate the selected image type
        const allowedTypes = [
            "image/png",
            "image/jpeg",
            "image/webp"
        ];

        if (!allowedTypes.includes(file.type)) {
            contactImage.value = "";
            fileName.textContent = "Please select a PNG, JPG or WEBP image.";
            imagePreview.src = "";
            imagePreview.classList.add("hidden");
            return;
        }

        // Display the filename
        fileName.textContent = file.name;

        // Generate the image preview
        previewUrl = URL.createObjectURL(file);
        imagePreview.src = previewUrl;
        imagePreview.classList.remove("hidden");
    });
});
