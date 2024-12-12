document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData(e.target);
    const data = {
        email: formData.get('email'),
        password: formData.get('password'),
        fullName: formData.get('fullName'),
        passwordConfirm: formData.get('passwordConf'),
        phoneNumber: formData.get('phoneNumber')
    };
    console.log(data);

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert('Регистрация успешна!');
            window.location.href = '/login';
        } else {
            const error = await response.json();
            alert(error.message || 'Регистрация не удалась!');
        }
    } catch (error) {
        alert('Что-то пошло не так!');
        console.error('Ошибка:', error);
    }
});