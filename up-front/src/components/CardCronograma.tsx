interface Session {
    time: string;
    subject: string;
}

interface CardCronogramaProps {
    day: string;
    sessions: Session[];
}

export default function CardCronograma({ day, sessions }: CardCronogramaProps) {

    return(
        <div className="flex flex-col p-4 bg-white border border-gray-200 rounded-sm-custom shadow-sm">
            
            <h5 className="text-h5 font-semibold text-black border-b border-gray-200 pb-2 mb-3 capitalize">
                {day}
            </h5>

            <div className="flex flex-col gap-4">
                {sessions.map((ses, index) => (
                    <div key={index} className="flex flex-col">
                        {/* Horário (Menor e cinza) */}
                        <span className="text-xs font-medium text-gray-500">
                            {ses.time}
                        </span>
                        {/* Nome da Disciplina (Negrito) */}
                        <span className="text-sm font-semibold text-black mt-0.5">
                            {ses.subject}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    )

}