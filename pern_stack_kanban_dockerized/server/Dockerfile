FROM node:22-alpine

WORKDIR /usr/src/app

# Copy package.json and package-lock.json (if present)
COPY package*.json ./

# Install npm dependencies and nodemon globally

RUN npm install && npm install -g nodemon

# Copy the rest of the application code
COPY . .

# Expose port 8000
EXPOSE 8000

# Use nodemon to start the server with legacy watch mode
CMD ["nodemon", "--config", "nodemon.json"]