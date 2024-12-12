const token = localStorage.getItem('token');
if (!token) {
    window.location.replace('/login');
}

const originalFetch = window.fetch;
window.fetch = function(...args) {
    const [url, config = {}] = args;
    if (url.startsWith('/home')) {
        config.headers = {
            ...config.headers,
            'Authorization': `Bearer ${token}`
        };
    }
    return originalFetch(url, config);
};