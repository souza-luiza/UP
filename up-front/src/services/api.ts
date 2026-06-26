const API_URL = "http://localhost:8081";

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
        `${API_URL}/auth/login`,
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
        `${API_URL}/subjects`,
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
        `${API_URL}/subjects`,
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
        `${API_URL}/availabilities`,
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
        `${API_URL}/availabilities`,
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
    const response = await fetch(`${API_URL}/schedule/generate`, {
        method: "POST",
        headers: authHeaders()
    });

    if (!response.ok) {
        throw new Error("Erro ao gerar cronograma");
    }

    return response.json();
}

export async function getFlashcards() {
    const response = await fetch(
        `${API_URL}/flashcards`,
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
        `${API_URL}/flashcards`,
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
        `${API_URL}/flashcards/review`,
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
        `${API_URL}/flashcards/${id}/review`,
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