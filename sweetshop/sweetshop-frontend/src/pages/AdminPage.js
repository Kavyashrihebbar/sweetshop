import React, { useState, useEffect } from "react";
import { getSweets, addSweet, updateSweet, deleteSweet, restockSweet } from "../api/api";

export default function AdminPage() {
  const [sweets, setSweets] = useState([]);
  const [form, setForm] = useState({ name: "", category: "", price: "", quantity: "" });

  useEffect(() => {
    loadSweets();
  }, []);

  const loadSweets = async () => {
    const data = await getSweets();
    setSweets(data);
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    await addSweet(form);
    setForm({ name: "", category: "", price: "", quantity: "" });
    await loadSweets();
  };

  const handleEdit = async (sweet) => {
    const name = prompt("Enter new name:", sweet.name);
    const category = prompt("Enter new category:", sweet.category);
    const price = prompt("Enter new price:", sweet.price);
    const quantity = prompt("Enter new quantity:", sweet.quantity);

    if (name && category && price && quantity) {
      await updateSweet(sweet.id, { name, category, price, quantity });
      await loadSweets();
    }
  };

  const handleRestock = async (sweet) => {
    const qty = prompt(`Enter quantity to restock for ${sweet.name}:`, 10);
    if (!qty) return;
    await restockSweet(sweet.id, parseInt(qty));
    await loadSweets();
  };

  const handleDelete = async (id) => {
    await deleteSweet(id);
    await loadSweets();
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>🍭 Admin Dashboard</h1>

      <form onSubmit={handleAdd} style={{ marginBottom: "20px" }}>
        <input
          placeholder="Name"
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
        />
        <input
          placeholder="Category"
          value={form.category}
          onChange={(e) => setForm({ ...form, category: e.target.value })}
        />
        <input
          type="number"
          placeholder="Price"
          value={form.price}
          onChange={(e) => setForm({ ...form, price: e.target.value })}
        />
        <input
          type="number"
          placeholder="Quantity"
          value={form.quantity}
          onChange={(e) => setForm({ ...form, quantity: e.target.value })}
        />
        <button type="submit">Add Sweet</button>
      </form>

      <div style={{ display: "flex", flexWrap: "wrap", gap: "15px" }}>
        {sweets.map((s) => (
          <div key={s.id} style={{ border: "1px solid #ccc", padding: "10px", borderRadius: "10px" }}>
            <h3>{s.name}</h3>
            <p>Category: {s.category}</p>
            <p>Price: ₹{s.price}</p>
            <p>Quantity: {s.quantity}</p>
            <button onClick={() => handleEdit(s)}>✏️ Edit</button>
            <button onClick={() => handleRestock(s)}>🔄 Restock</button>
            <button onClick={() => handleDelete(s.id)}>🗑️ Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
}
