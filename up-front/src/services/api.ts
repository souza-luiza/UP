const API_URL = process.env.NEXT_PUBLIC_API_URL;

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
    const response = await fetch(`${API_URL}/auth/login`,
        {
            method: "POST",
            credentials: "include",
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
    const response = await authFetch(`${API_URL}/subjects`,
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
    const response = await authFetch(`${API_URL}/subjects`,
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
    const response = await authFetch(`${API_URL}/availabilities`,
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
    const response = await authFetch(`${API_URL}/availabilities`,
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

export async function generateSchedule() {
    const response = await authFetch(`${API_URL}/schedule/generate`,
        {
            method: "POST",
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao gerar cronograma");
    }

    return response.json();
}

export async function getFlashcards() {
    const response = await authFetch(`${API_URL}/flashcards`,
        {
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao buscar flashcards");
    }

    return response.json();
}

export async function createFlashcard(
    question: string,
    answer: string
) {
    const response = await authFetch(`${API_URL}/flashcards`,
        {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify({
                question,
                answer
            })
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao criar flashcard");
    }

    return response.json();
}

export async function getFlashcardsForReview() {

    const response = await authFetch(`${API_URL}/flashcards/review`,
        {
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao buscar revisões");
    }

    return response.json();
}

export async function reviewFlashcard(
    id: number,
    correct: boolean
) {

    const response = await authFetch(`${API_URL}/flashcards/${id}/review`,
        {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify({
                correct
            })
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao revisar flashcard");
    }

    return response.json();
}

export async function getMe() {

    const response = await authFetch(`${API_URL}/users/me`,
        {
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao buscar usuário");
    }

    return response.json();
}

export async function logout() {

    try {

        await fetch(`${API_URL}/users/me/auth/logout`,
            {
                method: "POST",
                credentials: "include"
            }
        );

    } finally {

        localStorage.removeItem("accessToken");

        window.location.href = "/login";

    }

}

export async function deleteSubject(id: number) {

    const response = await authFetch(`${API_URL}/subjects/${id}`,
        {
            method: "DELETE",
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao excluir disciplina");
    }
}

export async function deleteAvailability(id: number) {

    const response = await authFetch(`${API_URL}/availabilities/${id}`,
        {
            method: "DELETE",
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao excluir disponibilidade");
    }

}

export async function deleteFlashcard(id: number) {
    const response = await authFetch(`${API_URL}/flashcards/${id}`,
        {
            method: "DELETE",
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao excluir flashcard");
    }
}

async function refreshToken() {

    const response = await fetch(`${API_URL}/auth/refresh`,
        {
            method: "POST",
            credentials: "include"
        }
    );

    if (!response.ok) {
        return false;
    }

    const data = await response.json();

    localStorage.setItem("accessToken", data.accessToken);

    return true;
}

async function authFetch(
    url: string,
    options: RequestInit = {}
) {

    let response = await fetch(url, {
        ...options,
        headers: {
            ...options.headers,
            Authorization: `Bearer ${getToken()}`
        },
        credentials: "include"
    });

    if (response.status !== 401) {
        return response;
    }

    const refreshed = await refreshToken();

    if (!refreshed) {

        logout();

        throw new Error("Sessão expirada");

    }

    response = await fetch(url, {
        ...options,
        headers: {
            ...options.headers,
            Authorization: `Bearer ${getToken()}`
        },
        credentials: "include"
    });

    return response;
}