document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById("loginForm");
    
    if (loginForm) {
        loginForm.addEventListener("submit", async function(event) {
            event.preventDefault();
            
            try {
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ 
                        email: document.getElementById("username").value,
                        password: document.getElementById("password").value 
                    })
                });
        
                const data = await response.json();
                
                if (response.ok) {
                    localStorage.setItem('token', data.token);
                    window.location.replace('/home');
                }
            } catch (error) {
                showError(error.message);
            }
        });
    }
});

function showError(message) {
    const modal = document.getElementById("errorModal");
    const errorMessage = document.getElementById("errorMessage");
    
    if (errorMessage && modal) {
        errorMessage.textContent = message;
        modal.style.display = "flex";
    }
}