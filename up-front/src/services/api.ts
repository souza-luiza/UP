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

export async function generateSchedule() {
    const response = await fetch(
        "http://localhost:8080/schedule/generate",
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
    const response = await fetch(
        "http://localhost:8080/flashcards",
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
    const response = await fetch(
        "http://localhost:8080/flashcards",
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

    const response = await fetch(
        "http://localhost:8080/flashcards/review",
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

    const response = await fetch(
        `http://localhost:8080/flashcards/${id}/review`,
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

    const response = await fetch(
        "http://localhost:8080/users/me",
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

        await fetch(
            "http://localhost:8080/auth/logout",
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

    const response = await fetch(
        `http://localhost:8080/subjects/${id}`,
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

    const response = await fetch(
        `http://localhost:8080/availabilities/${id}`,
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
    const response = await fetch(
        `http://localhost:8080/flashcards/${id}`,
        {
            method: "DELETE",
            headers: authHeaders()
        }
    );

    if (!response.ok) {
        throw new Error("Erro ao excluir flashcard");
    }
}