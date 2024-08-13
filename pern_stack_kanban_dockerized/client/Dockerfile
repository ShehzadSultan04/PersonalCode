# Use Node.js base image
FROM node:22-alpine

# Set working directory
WORKDIR /usr/src/app

# Install dependencies
COPY package*.json ./
RUN npm install

RUN npm install -D tailwindcss postcss autoprefixer postcss-loader
RUN npx tailwindcss init -p

# Copy application files
COPY . .

# Expose port used by Webpack Dev Server
EXPOSE 3000

# Start Webpack Dev Server
CMD ["npm", "start"]