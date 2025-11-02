import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link,
  useNavigate,
  Navigate,
} from "react-router-dom";
import HomePage from "./pages/HomePage";
import AdminPage from "./pages/AdminPage";
import AuthPage from "./pages/AuthPage";

// -------------------------
// ✅ Beautiful Navbar
// -------------------------
function Navbar() {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user"));

  const handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    navigate("/auth");
  };

  const linkStyle = {
    color: "white",
    textDecoration: "none",
    fontWeight: "500",
    transition: "0.3s",
  };

  return (
    <nav
      style={{
        background: "linear-gradient(90deg, #ff758c, #ff7eb3)",
        color: "white",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "14px 40px",
        boxShadow: "0 3px 10px rgba(0,0,0,0.15)",
        position: "sticky",
        top: 0,
        zIndex: 1000,
      }}
    >
      {/* Left side - Logo */}
      <div
        style={{
          fontSize: "24px",
          fontWeight: "bold",
          cursor: "pointer",
          display: "flex",
          alignItems: "center",
          gap: "8px",
        }}
        onClick={() => navigate("/")}
      >
        🍬 Sweet Shop
      </div>

      {/* Right side - Links */}
      <div style={{ display: "flex", alignItems: "center", gap: "20px" }}>
        {user ? (
          <>
            {user.role === "ADMIN" ? (
              <Link to="/admin" style={linkStyle}>
                🧁 Admin Dashboard
              </Link>
            ) : (
              <Link to="/" style={linkStyle}>
                🏠 Home
              </Link>
            )}
            <span style={{ fontWeight: "500" }}>
              👤 {user.username} ({user.role})
            </span>
            <button
              onClick={handleLogout}
              style={{
                background: "white",
                color: "#ff758c",
                border: "none",
                borderRadius: "6px",
                padding: "6px 14px",
                fontWeight: "bold",
                cursor: "pointer",
                transition: "0.3s",
              }}
              onMouseOver={(e) => (e.target.style.background = "#ffe0ec")}
              onMouseOut={(e) => (e.target.style.background = "white")}
            >
              🚪 Logout
            </button>
          </>
        ) : (
          <Link
            to="/auth"
            style={{
              ...linkStyle,
              background: "white",
              color: "#ff758c",
              borderRadius: "6px",
              padding: "6px 12px",
            }}
          >
            🔐 Login / Register
          </Link>
        )}
      </div>
    </nav>
  );
}

// -------------------------
// ✅ Private Route Logic
// -------------------------
function PrivateRoute({ children, role }) {
  const user = JSON.parse(localStorage.getItem("user"));
  if (!user) return <Navigate to="/auth" replace />;
  if (role && user.role !== role) return <Navigate to="/" replace />;
  return children;
}

// -------------------------
// ✅ App Component
// -------------------------
export default function App() {
  return (
    <Router>
      <Navbar />
      <div style={{ padding: "20px" }}>
        <Routes>
          {/* USER Home Page */}
          <Route
            path="/"
            element={
              <PrivateRoute role="USER">
                <HomePage />
              </PrivateRoute>
            }
          />

          {/* ADMIN Dashboard */}
          <Route
            path="/admin"
            element={
              <PrivateRoute role="ADMIN">
                <AdminPage />
              </PrivateRoute>
            }
          />

          {/* LOGIN / REGISTER */}
          <Route path="/auth" element={<AuthPage />} />
        </Routes>
      </div>
    </Router>
  );
}
