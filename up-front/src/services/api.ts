const API_URL = "http://localhost:8080";

export async function register(
    name: string,
    email: string,
    password: string
) {
    const response = await fetch(`${API_URL}/users`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            name,
            email,
            password,
        }),
    });

    if (!response.ok) {
        throw new Error("Erro ao cadastrar usuário");
    }

    return response.json();
}