# GitHub Setup Guide - Public Repository with JAR Download

## Step-by-Step Instructions

### Step 1: Build the JAR File

```bash
# Clean and build the project
mvn clean package

# The JAR will be created at: target/webhook-solver-1.0.0.jar
```

### Step 2: Create a GitHub Repository

1. Go to [GitHub.com](https://github.com) and sign in
2. Click the **"+"** icon in the top right → **"New repository"**
3. Repository settings:
   - **Repository name**: `webhook-solver` (or your preferred name)
   - **Description**: "Spring Boot app for webhook SQL problem solver"
   - **Visibility**: Select **Public** (required for raw links)
   - **DO NOT** initialize with README, .gitignore, or license
4. Click **"Create repository"**

### Step 3: Initialize Git and Push Code

```bash
# Navigate to your project directory
cd C:\Users\rushi\Desktop\bajaj

# Initialize git repository
git init

# Add all files (code + JAR)
git add .

# Commit files
git commit -m "Initial commit: Spring Boot webhook solver with JAR"

# Add remote repository (replace YOUR_USERNAME with your GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/webhook-solver.git

# Push to GitHub
git branch -M main
git push -u origin main
```

### Step 4: Create a Release for the JAR (Recommended)

1. Go to your repository on GitHub
2. Click **"Releases"** → **"Create a new release"**
3. Fill in:
   - **Tag version**: `v1.0.0`
   - **Release title**: `Webhook Solver v1.0.0`
   - **Description**: Add release notes
4. Upload the JAR file:
   - Drag and drop `target/webhook-solver-1.0.0.jar` into the "Attach binaries" section
5. Click **"Publish release"**

### Step 5: Get the Raw GitHub Links

#### Option A: Direct JAR File Link (from main branch)
```
https://raw.githubusercontent.com/YOUR_USERNAME/webhook-solver/main/target/webhook-solver-1.0.0.jar
```

#### Option B: Release Download Link (Recommended)
```
https://github.com/YOUR_USERNAME/webhook-solver/releases/download/v1.0.0/webhook-solver-1.0.0.jar
```

#### Option C: GitHub Raw Link (if JAR is in root)
If you put the JAR in the root directory:
```
https://raw.githubusercontent.com/YOUR_USERNAME/webhook-solver/main/webhook-solver-1.0.0.jar
```

### Step 6: Alternative - Upload JAR to Root Directory

If you want the JAR in the root for easier access:

```bash
# Copy JAR to root
copy target\webhook-solver-1.0.0.jar webhook-solver-1.0.0.jar

# Add and commit
git add webhook-solver-1.0.0.jar
git commit -m "Add JAR file to root directory"
git push
```

Then use:
```
https://raw.githubusercontent.com/YOUR_USERNAME/webhook-solver/main/webhook-solver-1.0.0.jar
```

## Quick Commands Summary

```bash
# Build JAR
mvn clean package

# Initialize Git (if not done)
git init
git add .
git commit -m "Initial commit"

# Connect to GitHub (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/webhook-solver.git
git branch -M main
git push -u origin main

# Copy JAR to root (optional)
copy target\webhook-solver-1.0.0.jar webhook-solver-1.0.0.jar
git add webhook-solver-1.0.0.jar
git commit -m "Add JAR to root"
git push
```

## Submission Format

When submitting, use this format:

```
GitHub Repository: https://github.com/YOUR_USERNAME/webhook-solver.git

JAR Download Link: https://raw.githubusercontent.com/YOUR_USERNAME/webhook-solver/main/webhook-solver-1.0.0.jar
```

Or if using releases:

```
GitHub Repository: https://github.com/YOUR_USERNAME/webhook-solver.git

JAR Download Link: https://github.com/YOUR_USERNAME/webhook-solver/releases/download/v1.0.0/webhook-solver-1.0.0.jar
```

## Notes

- Make sure the repository is **Public** (not private) for raw links to work
- The JAR file should be downloadable directly via the raw link
- Test the link by opening it in a browser - it should download the JAR file
- If the link doesn't work, check that:
  1. Repository is public
  2. File path is correct
  3. File has been pushed to GitHub

