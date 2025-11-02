const API_BASE_URL = "http://localhost:8080/api";

// ---------------------
// Public APIs
// ---------------------

// ✅ Get all sweets (visible to everyone)
export async function getSweets() {
  const response = await fetch(`${API_BASE_URL}/sweets`);
  if (!response.ok) throw new Error("Failed to fetch sweets");
  return response.json();
}

// ✅ Register a new user or admin
export async function registerUser(data) {
  const response = await fetch(`${API_BASE_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || "Registration failed");
  }

  return response.json();
}

// ✅ Login user or admin
export async function loginUser(credentials) {
  const response = await fetch(`${API_BASE_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(credentials),
  });

  if (!response.ok) {
    const errText = await response.text();
    throw new Error(errText || "Invalid credentials");
  }

  return response.json(); // Should include { token, username, role, message }
}

// ---------------------
// User Actions
// ---------------------

// ✅ Purchase sweet
export async function purchaseSweet(id, quantity = 1) {
  const response = await fetch(`${API_BASE_URL}/sweets/${id}/purchase`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ quantity }),
  });

  if (!response.ok) throw new Error("Failed to purchase sweet");
  return response.json();
}

// ---------------------
// Admin Actions
// ---------------------

// ✅ Helper to include Authorization header
function getAuthHeaders() {
  const token = localStorage.getItem("token");
  return {
    "Content-Type": "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}), // ✅ attach token if exists
  };
}

// ✅ Add sweet (admin)
export async function addSweet(sweet) {
  const response = await fetch(`${API_BASE_URL}/sweets`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(sweet),
  });

  if (!response.ok) throw new Error("Failed to add sweet");
  return response.json();
}

// ✅ Update sweet (admin)
export async function updateSweet(id, sweet) {
  const response = await fetch(`${API_BASE_URL}/sweets/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(sweet),
  });

  if (!response.ok) throw new Error("Failed to update sweet");
  return response.json();
}

// ✅ Delete sweet (admin)
export async function deleteSweet(id) {
  const response = await fetch(`${API_BASE_URL}/sweets/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    const errorText = await response.text();
    console.error("❌ Failed to delete sweet:", response.status, errorText);
    throw new Error("Failed to delete sweet");
  }

  return true;
}

// ✅ Restock sweet (admin)
export async function restockSweet(id, quantity) {
  const response = await fetch(`${API_BASE_URL}/sweets/${id}/restock`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify({ quantity }),
  });

  if (!response.ok) {
    const text = await response.text();
    console.error("❌ Failed to restock sweet:", text);
    throw new Error("Failed to restock sweet");
  }

  return response.json();
}
