
# Nuxt Frontend Project

## Overview
This is a frontend application built using Nuxt 3 and styled with Tailwind CSS. The project is designed to work in conjunction with a backend service, providing a dynamic and responsive web interface.

## Prerequisites
- Node.js 18+
- npm or yarn package manager

## Setup Instructions

### Clone the Repositories
1. Clone the backend repository:
   ```bash
   git clone https://gitlab.ti.howest.be/ti/2024-2025/s5/project-iv/projecten/project-14/backend
   ```

2. Clone the frontend repository:
   ```bash
   git clone https://gitlab.ti.howest.be/ti/2024-2025/s5/project-iv/projecten/project-14/frontend
   ```

3. Refer to the documentation for additional details:
   [Project Documentation](https://gitlab.ti.howest.be/ti/2024-2025/s5/project-iv/projecten/project-14/documentation#)

### Set Up the Frontend
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install the dependencies:
   ```bash
   npm install
   ```

3. Edit the apiUrl in /config/config.js to the url of the server and the webSocketUrl to the url of the websocket:
   ```js
   export const apiUrl = "http://SERVERIP:8080";
   export const webSocketUrl = "ws://WEBSOCKETIP:3001";
   ```

4. Start the development server:
   ```bash
   npm run dev
   ```
   - The application will be available at `http://localhost:3000`.

### Build for Production
To create a production-ready build, run:
```bash
npm run build
```
The generated files will be in the `.output` directory. You can preview the build using:
```bash
npm run preview
```

### Backend Dependency
Ensure the backend service is running before using the frontend. Follow the setup instructions for the backend available in its repository:
[Backend Repository](https://gitlab.ti.howest.be/ti/2024-2025/s5/project-iv/projecten/project-14/backend)

## Project Structure
This project uses Nuxt's file-based routing with pages and components:
- **Pages**: Located in the `pages` directory, these define the application's routes.
- **Components**: Reusable UI components are stored in the `components` directory.

## Dependencies
Key dependencies used in this project:
- **Nuxt 3**: Framework for building Vue applications.
- **Tailwind CSS**: Utility-first CSS framework for styling.
- **Pinia**: State management library.
- **Axios**: HTTP client for API requests.
- **GSAP**: Animation library.
- **Chart.js**: Library for creating charts.
- **Font Awesome**: Icon library.

## Development Tools
- **daisyUI**: Tailwind CSS component library.
- **PostCSS** and **Autoprefixer**: CSS processing tools.


# Nuxt 3 Minimal Starter

Look at the [Nuxt 3 documentation](https://nuxt.com/docs/getting-started/introduction) to learn more.

## Setup

Make sure to install the dependencies:

```bash
# npm
npm install

# pnpm
pnpm install

# yarn
yarn install

# bun
bun install
```

## Development Server

Start the development server on `http://localhost:3000`:

```bash
# npm
npm run dev

# pnpm
pnpm run dev

# yarn
yarn dev

# bun
bun run dev
```

## Production

Build the application for production:

```bash
# npm
npm run build

# pnpm
pnpm run build

# yarn
yarn build

# bun
bun run build
```

Locally preview production build:

```bash
# npm
npm run preview

# pnpm
pnpm run preview

# yarn
yarn preview

# bun
bun run preview
```

Check out the [deployment documentation](https://nuxt.com/docs/getting-started/deployment) for more information.