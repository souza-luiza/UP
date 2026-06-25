interface Session {
    time: string;
    subject: string;
}

interface CardCronogramaProps {
    day: string;
    sessions: Session[];
}

export default function CardCronograma({ day, sessions }: CardCronogramaProps) {

    function formatTimeRange(timeRange: string) {
        if (!timeRange.includes(" - ")) return timeRange;
        
        const [start, end] = timeRange.split(" - ");
        const formatTime = (timeStr: string) => timeStr?.slice(0, 5) || timeStr;
        
        return `${formatTime(start)} - ${formatTime(end)}`;
    }

    return(
        <div className="flex flex-col p-5 bg-white border border-secondary-dark/20 rounded-md-custom shadow-sm w-full md:w-80 min-h-[200px]">
            
            <h5 className="text-h5 font-semibold text-black border-b border-slate-100 pb-2 mb-3 capitalize">
                {day}
            </h5>

            <div className="flex flex-col gap-4">
                {sessions.map((ses, index) => (
                    <div key={index} className="flex flex-col border-l-2 border-primary/40 pl-3 py-0.5">
                        {/* Horário (Menor e cinza) */}
                        <span className="text-xs font-semibold text-neutral-500">
                            {formatTimeRange(ses.time)}
                        </span>
                        {/* Nome da Disciplina (Negrito) */}
                        <span className="text-p font-medium text-black mt-0.5">
                            {ses.subject}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    )

}