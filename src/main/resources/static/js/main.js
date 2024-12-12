function apiRequest(url, options = {}) {
    const token = localStorage.getItem('token');
    return fetch(url, {
        ...options,
        headers: {
            ...options.headers,
            'Authorization': token ? `Bearer ${token}` : '',
            'Content-Type': 'application/json'
        }
    });
}

function addAuthHeader(headers = {}) {
    const token = localStorage.getItem('token');
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    return headers;
}

const originalFetch = window.fetch;
window.fetch = async function() {
    let [url, config = {}] = arguments;
    config.headers = addAuthHeader(config.headers || {});
    const response = await originalFetch(url, config);
    if (response.status === 401) {
        localStorage.removeItem('token');
        window.location.replace('/login');
    }
    return response;
};