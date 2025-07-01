let selectedServiceType = "";

async function showForm(serviceType) {
    selectedServiceType = serviceType;
    document.getElementById("paymentForm").style.display = "block";
    document.getElementById("selectedService").value = serviceType;

    const iconMap = {
        mobile: "https://img.icons8.com/?size=100&id=ix1ZYFj8mZoq&format=png&color=000000",
        internet: "https://img.icons8.com/?size=100&id=16109&format=png&color=000000",
        landline: "https://img.icons8.com/?size=100&id=AADv7SuOc0AR&format=png&color=000000",
        donation: "https://img.icons8.com/?size=100&id=lf0FBpL42j08&format=png&color=000000"
    };

    document.getElementById("payServiceIcon").src = iconMap[serviceType] || "";
    document.getElementById("payServiceIcon").style.display = "inline";

    // Show relevant fields
    document.getElementById("mobileField").style.display = serviceType === "mobile" ? "block" : "none";
    document.getElementById("landlineField").style.display =
        (serviceType === "landline" || serviceType === "internet") ? "block" : "none";

    const providerSelect = document.getElementById("providerId");
    const amountInput = document.getElementById("amount");
    providerSelect.innerHTML = '<option value="">-- Select Provider --</option>';
    amountInput.value = "";

    // Allow manual amount entry for mobile or donation
    amountInput.readOnly = !(serviceType === "mobile" || serviceType === "donation");

    try {
        const response = await fetch(`/api/providers?type=${serviceType}`);
        const providers = await response.json();
        providers.forEach(provider => {
            const option = document.createElement("option");
            option.value = provider.id;
            option.textContent = provider.name;
            option.dataset.price = provider.price;
            providerSelect.appendChild(option);
        });
    } catch (error) {
        console.error("Error loading providers:", error);
        alert("Could not load providers. Please try again later.");
    }
}

function updateAmount() {
    const providerSelect = document.getElementById("providerId");
    const amountInput = document.getElementById("amount");
    const selectedOption = providerSelect.options[providerSelect.selectedIndex];

    if (!selectedOption || selectedOption.value === "") {
        amountInput.value = "";
        amountInput.readOnly = !(selectedServiceType === "mobile" || selectedServiceType === "donation");
        return;
    }

    if (selectedServiceType === "mobile" || selectedServiceType === "donation") {
        // Leave it editable, don’t override
        amountInput.readOnly = false;
    } else {
        // Set fixed price
        amountInput.value = selectedOption.dataset.price || "";
        amountInput.readOnly = true;
    }
}

function validateForm() {
    if (selectedServiceType === "mobile") {
        const phone = document.getElementById("phone").value.trim();
        if (!phone) {
            alert("Phone number is required.");
            return false;
        }
    }
    if (selectedServiceType === "landline" || selectedServiceType === "internet") {
        const landline = document.getElementById("landline").value.trim();
        if (!landline) {
            alert("Telephone number is required.");
            return false;
        }
    }
    return true;
}

function loadPayPalSDK() {
    const clientId = document.getElementById("clientId").value.trim();
    const amount = document.getElementById("smartAmount").value.trim();

    if (!clientId || !amount || parseFloat(amount) <= 0) {
        alert("Please enter a valid Receiver Client ID and amount.");
        return;
    }

    // Remove old script and PayPal button
    const oldScript = document.getElementById("paypal-sdk");
    if (oldScript) oldScript.remove();
    document.getElementById("paypal-button-container").innerHTML = "";

    // Create new PayPal SDK script
    const script = document.createElement("script");
    script.src = `https://www.paypal.com/sdk/js?client-id=${clientId}&currency=USD`;
    script.id = "paypal-sdk";

    script.onload = renderPayPalButtons;
    script.onerror = function () {
        alert("The Client ID may be invalid.");
    };

    document.head.appendChild(script);
}

function renderPayPalButtons() {
    const amount = document.getElementById("smartAmount").value;
    const description = document.getElementById("smartDescription").value;

    paypal.Buttons({
        createOrder: function (data, actions) {
            return actions.order.create({
                purchase_units: [{
                    description: description || 'Money Transfer',
                    amount: {
                        currency_code: 'USD',
                        value: amount
                    }
                }]
            });
        },
        onApprove: function (data, actions) {
            return actions.order.capture().then(function (details) {
                alert('Money sent by ' + details.payer.name.given_name);
                console.log('Transaction details:', details);
            });
        },
        onError: function (err) {
            console.error(err);
            alert("An error occurred during payment.");
        }
    }).render('#paypal-button-container');
}
