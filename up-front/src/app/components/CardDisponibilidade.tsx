import Button from "./Button";

interface CardDisponibilidadeProps {
    dayOfWeek: string;
    startTime: string;
    endTime: string;
    onDelete?: () => void;
}

export default function CardDisponibilidade({ dayOfWeek, startTime, endTime, onDelete }: CardDisponibilidadeProps) {

    return(
        <div className="inline-flex p-5 gap-2 flex-col justify-center items-start rounded-md-custom bg-secondary">
            <h4 className="text-h4 font-semibold text-black">{dayOfWeek}</h4>
            <p className="text-p font-regular text-black">
                {startTime} - {endTime}
            </p>
            {onDelete && (
                <Button onClick={onDelete} variant="outline">
                    Excluir
                </Button>
            )}
        </div>
    )

}