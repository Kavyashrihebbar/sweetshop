import React, { useEffect, useState } from "react";
import { getSweets, purchaseSweet } from "../api/api";

export default function HomePage() {
  const [sweets, setSweets] = useState([]);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("All");
  const [selectedSweet, setSelectedSweet] = useState(null); // ✅ store sweet being purchased
  const [quantity, setQuantity] = useState(1); // ✅ entered quantity
  const user = JSON.parse(localStorage.getItem("user"));

  useEffect(() => {
    loadSweets();
  }, []);

  async function loadSweets() {
    try {
      const data = await getSweets();
      setSweets(data);
    } catch (err) {
      setError(err.message);
    }
  }

  // 💸 Purchase confirm function
  const handleConfirmPurchase = async () => {
    if (!user) {
      alert("⚠️ Please login before purchasing!");
      return;
    }

    if (!quantity || quantity <= 0) {
      alert("⚠️ Please enter a valid quantity!");
      return;
    }

    try {
      await purchaseSweet(selectedSweet.id, quantity);
      alert(`✅ Purchased ${quantity} ${selectedSweet.name}(s) successfully!`);
      setSelectedSweet(null); // close quantity input
      setQuantity(1);
      await loadSweets();
    } catch (err) {
      alert("❌ " + err.message);
    }
  };

  // 🔍 Filter sweets
  const filteredSweets = sweets.filter((s) => {
    const matchesSearch = s.name.toLowerCase().includes(search.toLowerCase());
    const matchesCategory =
      category === "All" || s.category.toLowerCase() === category.toLowerCase();
    return matchesSearch && matchesCategory;
  });

  return (
    <div style={{ padding: "20px" }}>
      <h1>🍬 Sweet Shop Dashboard</h1>

      {/* Search & Filter */}
      <div
        style={{
          display: "flex",
          gap: "10px",
          marginBottom: "20px",
          alignItems: "center",
        }}
      >
        <input
          type="text"
          placeholder="Search sweets..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          style={{
            padding: "8px",
            width: "250px",
            border: "1px solid #ccc",
            borderRadius: "5px",
          }}
        />

        <select
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          style={{
            padding: "8px",
            border: "1px solid #ccc",
            borderRadius: "5px",
          }}
        >
          <option value="All">All</option>
          <option value="Milk">Milk</option>
          <option value="Chocolate">Chocolate</option>
          <option value="Dry Fruit">Dry Fruit</option>
          <option value="Sugar Free">Sugar Free</option>
        </select>
      </div>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* Sweet Cards */}
      <div
        style={{
          display: "flex",
          flexWrap: "wrap",
          gap: "20px",
        }}
      >
        {filteredSweets.length === 0 ? (
          <p>No sweets found 🍭</p>
        ) : (
          filteredSweets.map((s) => (
            <div
              key={s.id}
              style={{
                border: "1px solid #ccc",
                padding: "15px",
                borderRadius: "10px",
                width: "220px",
                textAlign: "center",
                boxShadow: "2px 2px 8px rgba(0,0,0,0.1)",
              }}
            >
              <h3>{s.name}</h3>
              <p>Category: {s.category}</p>
              <p>Price: ₹{s.price}</p>
              <p>Qty Available: {s.quantity}</p>

              {/* If this sweet is selected, show quantity input */}
              {selectedSweet?.id === s.id ? (
                <div>
                  <input
                    type="number"
                    min="1"
                    max={s.quantity}
                    value={quantity}
                    onChange={(e) => setQuantity(parseInt(e.target.value))}
                    style={{
                      width: "80px",
                      padding: "5px",
                      marginBottom: "8px",
                      border: "1px solid #ccc",
                      borderRadius: "5px",
                      textAlign: "center",
                    }}
                  />
                  <br />
                  <button
                    onClick={handleConfirmPurchase}
                    style={{
                      marginRight: "5px",
                      padding: "5px 10px",
                    }}
                  >
                    Confirm ✅
                  </button>
                  <button
                    onClick={() => setSelectedSweet(null)}
                    style={{ padding: "5px 10px" }}
                  >
                    Cancel ❌
                  </button>
                </div>
              ) : (
                <button
                  disabled={s.quantity === 0}
                  onClick={() => setSelectedSweet(s)}
                >
                  {s.quantity === 0 ? "Out of Stock" : "Purchase"}
                </button>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
