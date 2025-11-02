import React, { useState } from "react";
import { registerUser, loginUser } from "../api/api";
import { useNavigate } from "react-router-dom";

export default function AuthPage() {
  const [isLogin, setIsLogin] = useState(true);
  const [form, setForm] = useState({ username: "", password: "", role: "USER" });
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      if (isLogin) {
        const data = await loginUser(form);
        if (data.token) localStorage.setItem("token", data.token);
        localStorage.setItem("user", JSON.stringify(data));
        setMessage("✅ Login successful!");
        setTimeout(() => {
          if (data.role === "ADMIN") navigate("/admin");
          else navigate("/");
        }, 800);
      } else {
        await registerUser(form);
        setMessage("✅ Registration successful! Please login.");
        setIsLogin(true);
      }
    } catch (err) {
      setMessage("❌ " + (err.message || "Something went wrong"));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        background: "linear-gradient(120deg, #ff9a9e, #fad0c4)",
        fontFamily: "'Poppins', sans-serif",
        overflow: "hidden",
      }}
    >
      <div
        style={{
          width: "380px",
          height: "460px",
          perspective: "1000px",
        }}
      >
        <div
          style={{
            position: "relative",
            width: "100%",
            height: "100%",
            transition: "transform 0.8s",
            transformStyle: "preserve-3d",
            transform: isLogin ? "rotateY(0deg)" : "rotateY(180deg)",
          }}
        >
          {/* LOGIN CARD */}
          <div style={cardStyle}>
            <h2 style={titleStyle}>🍬 Sweet Shop Login</h2>
            <form onSubmit={handleSubmit}>
              <input
                type="text"
                placeholder="Username"
                value={form.username}
                onChange={(e) => setForm({ ...form, username: e.target.value })}
                style={inputStyle}
                required
                onFocus={(e) => (e.target.style.borderColor = "#ff7eb3")}
                onBlur={(e) => (e.target.style.borderColor = "#ddd")}
              />
              <input
                type="password"
                placeholder="Password"
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
                style={inputStyle}
                required
                onFocus={(e) => (e.target.style.borderColor = "#ff7eb3")}
                onBlur={(e) => (e.target.style.borderColor = "#ddd")}
              />

              <button type="submit" style={buttonStyle} disabled={loading}>
                {loading ? "Please wait..." : "Login"}
              </button>
            </form>

            {message && (
              <p
                style={{
                  marginTop: "15px",
                  color: message.startsWith("❌") ? "red" : "green",
                  fontWeight: "500",
                  fontSize: "14px",
                }}
              >
                {message}
              </p>
            )}

            <button
              onClick={() => setIsLogin(false)}
              style={switchStyle}
            >
              ✨ Need an account? Register
            </button>
          </div>

          {/* REGISTER CARD */}
          <div style={{ ...cardStyle, transform: "rotateY(180deg)" }}>
            <h2 style={titleStyle}>🍭 Create Your Account</h2>
            <form onSubmit={handleSubmit}>
              <input
                type="text"
                placeholder="Username"
                value={form.username}
                onChange={(e) => setForm({ ...form, username: e.target.value })}
                style={inputStyle}
                required
                onFocus={(e) => (e.target.style.borderColor = "#ff7eb3")}
                onBlur={(e) => (e.target.style.borderColor = "#ddd")}
              />
              <input
                type="password"
                placeholder="Password"
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
                style={inputStyle}
                required
                onFocus={(e) => (e.target.style.borderColor = "#ff7eb3")}
                onBlur={(e) => (e.target.style.borderColor = "#ddd")}
              />
              <select
                value={form.role}
                onChange={(e) => setForm({ ...form, role: e.target.value })}
                style={inputStyle}
              >
                <option value="USER">User</option>
                <option value="ADMIN">Admin</option>
              </select>

              <button type="submit" style={buttonStyle} disabled={loading}>
                {loading ? "Please wait..." : "Register"}
              </button>
            </form>

            {message && (
              <p
                style={{
                  marginTop: "15px",
                  color: message.startsWith("❌") ? "red" : "green",
                  fontWeight: "500",
                  fontSize: "14px",
                }}
              >
                {message}
              </p>
            )}

            <button
              onClick={() => setIsLogin(true)}
              style={switchStyle}
            >
              🔑 Already have an account? Login
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

// 💅 Shared Styles
const cardStyle = {
  position: "absolute",
  width: "100%",
  height: "100%",
  backfaceVisibility: "hidden",
  background: "rgba(255, 255, 255, 0.9)",
  borderRadius: "20px",
  boxShadow: "0 8px 25px rgba(0, 0, 0, 0.15)",
  padding: "40px 30px",
  textAlign: "center",
  backdropFilter: "blur(10px)",
};

const titleStyle = {
  color: "#ff5f7e",
  marginBottom: "25px",
  fontWeight: "700",
  fontSize: "22px",
};

const inputStyle = {
  width: "100%",
  padding: "10px",
  marginBottom: "12px",
  borderRadius: "8px",
  border: "1px solid #ddd",
  outline: "none",
  fontSize: "15px",
  transition: "border-color 0.3s, box-shadow 0.3s",
  boxSizing: "border-box",
};

const buttonStyle = {
  width: "100%",
  padding: "12px",
  background: "linear-gradient(90deg, #ff758c, #ff7eb3)",
  border: "none",
  borderRadius: "8px",
  color: "white",
  fontWeight: "600",
  fontSize: "16px",
  cursor: "pointer",
  marginTop: "10px",
  boxShadow: "0 4px 10px rgba(255,118,140,0.4)",
  transition: "transform 0.2s, opacity 0.2s",
};

const switchStyle = {
  marginTop: "20px",
  background: "none",
  border: "none",
  color: "#ff5f7e",
  fontWeight: "600",
  cursor: "pointer",
  fontSize: "15px",
  transition: "color 0.3s",
};
