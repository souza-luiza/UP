const API_URL = "http://localhost:8080";

function getToken() {
    return localStorage.getItem("accessToken");
}

function authHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${getToken()}`
    };
}

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

export async function login(
    email: string,
    password: string
) {
    const response = await fetch(
        "http://localhost:8080/auth/login",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email,
                password,
            }),
        }
    );

    if (!response.ok) {
        throw new Error("Credenciais inválidas");
    }

    return response.json();
}

export async function getSubjects() {
    const response = await fetch(
        "http://localhost:8080/subjects",
        {
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao buscar disciplinas");
    }

    return response.json();
}

export async function createSubject(
    name: string,
    difficulty: number
) {
    const response = await fetch(
        "http://localhost:8080/subjects",
        {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify({
                name,
                difficulty
            })
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao criar disciplina");
    }

    return response.json();
}

export async function getAvailabilities() {
    const response = await fetch(
        "http://localhost:8080/availabilities",
        {
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao buscar disponibilidades");
    }

    return response.json();
}

export async function createAvailability(
    dayOfWeek: string,
    startTime: string,
    endTime: string
) {
    const response = await fetch(
        "http://localhost:8080/availabilities",
        {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify({
                dayOfWeek,
                startTime,
                endTime
            })
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao criar disponibilidade");
    }

    return response.json();
}