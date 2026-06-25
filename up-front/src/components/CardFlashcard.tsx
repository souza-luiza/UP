interface CardFlashcardProps {
    question: string;
    answer: string;
    level: number;
    onDelete?: () => void;
}

export default function CardFlashcard({ question, answer, level, onDelete }: CardFlashcardProps) {
    return (
        <div className="flex flex-col p-4 bg-white border border-gray-200 rounded-sm-custom shadow-sm max-w-sm justify-between gap-4">
            <div className="flex flex-col gap-2">
                <div>
                    <span className="text-xs font-semibold text-gray-500 uppercase tracking-wider">Pergunta:</span>
                    <p className="text-sm font-medium text-black mt-0.5 whitespace-pre-wrap">{question}</p>
                </div>
                
                <div className="border-t border-gray-100 pt-2">
                    <span className="text-xs font-semibold text-gray-500 uppercase tracking-wider">Resposta:</span>
                    <p className="text-sm text-gray-700 mt-0.5 whitespace-pre-wrap">{answer}</p>
                </div>
            </div>

            <div className="flex items-center justify-between border-t border-gray-100 pt-2 mt-auto">
                <span className="text-sm font-semibold text-black">
                    Nível: <span className="text-gray-600">{level}</span>
                </span>
                
                {onDelete && (
                    <button 
                        onClick={onDelete}
                        className="text-xs font-medium text-red-500 hover:underline cursor-pointer"
                    >
                        Excluir
                    </button>
                )}
            </div>
        </div>
    );
}