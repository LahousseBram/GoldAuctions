<template>
  <footer class="footer">
    <div class="footer-section contact-info">
      <div class="contact-item">
        <i class="icon email-icon"></i>
        <span>XXX@gmail.com</span>
      </div>
      <div class="contact-item">
        <i class="icon phone-icon"></i>
        <span>+110000000000</span>
      </div>
    </div>

    <div class="footer-section social-links">
      <div class="social-item">
        <i class="icon facebook-icon"></i>
        <span>www.facebook.com</span>
      </div>
      <div class="social-item">
        <i class="icon instagram-icon"></i>
        <span>www.instagram.com</span>
      </div>
    </div>

    <div class="footer-section nav-links">
      <ul>
        <li><a href="/">Home</a></li>
        <li><a href="/about">About us</a></li>
        <li><a href="/contact">Contact us</a></li>
      </ul>
    </div>

    <div class="footer-section auth-links">
      <ul>
        <li><a href="/login">Log in</a></li>
        <li><a href="/signup">Sign up</a></li>
      </ul>
    </div>

    <div class="footer-section copyright">
      <span>&copy; 2024</span>
    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
.footer {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  padding: 20px;
  background-color: #333;
  font-family: Arial, sans-serif;
  color: white;
}

.footer-section {
  flex: 1;
  padding: 10px;
}

.contact-info, .social-links {
  display: flex;
  flex-direction: column;
}

.contact-item, .social-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.icon {
  font-size: 24px;
  margin-right: 8px;
}

.email-icon::before {
  content: "✉️";
}

.phone-icon::before {
  content: "📱";
}

.facebook-icon::before {
  content: "🌐";
}

.instagram-icon::before {
  content: "📷";
}

.nav-links ul, .auth-links ul {
  list-style: none;
  padding: 0;
}

.nav-links li, .auth-links li {
  margin-bottom: 8px;
}

.nav-links a, .auth-links a {
  text-decoration: none;
  color: white;
}

.nav-links a:hover, .auth-links a:hover {
  color: #000;
}

.copyright {
  margin-top: 3rem;
  text-align: center;
  font-size: 0.9rem;
  color: #666;
}
</style>
