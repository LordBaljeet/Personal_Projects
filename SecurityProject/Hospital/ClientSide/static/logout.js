// logout.js

// Function to send a logout request to the server
function logoutUser() {
    fetch('/logout/', {
      method: 'POST',
      headers: {
        'X-CSRFToken': getCSRFToken(),  // Include CSRF token
      },
    })
    .then(response => {
      if (response.ok) {
        // Logout successful, redirect to the desired page
        window.location.href = '/';
      } else {
        // Handle logout failure
        console.error('Logout request failed.');
      }
    })
    .catch(error => {
      console.error('Logout request failed:', error);
    });
  }
  
  // Function to retrieve the CSRF token from cookies
  function getCSRFToken() {
    const csrfCookie = document.cookie
      .split(';')
      .find(cookie => cookie.trim().startsWith('csrftoken='));
    if (csrfCookie) {
      return csrfCookie.split('=')[1];
    }
    return null;
  }
  
  // Logout the user when the window is closed or unloaded
  window.addEventListener('beforeunload', logoutUser);
  
  // Logout the user after 30 minutes of inactivity
  setTimeout(logoutUser, 30 * 60 * 1000);  // 30 minutes
  