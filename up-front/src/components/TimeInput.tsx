import { InputHTMLAttributes } from "react";

interface TimeInputProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type'> {
    label?: string;
}

export default function TimeInput({ label, id, ...props }: TimeInputProps) {
    return(
        <div className="flex flex-col items-start">
            
            {label && (
                <label htmlFor={id} className="text-h5">
                    {label}
                </label>
            )}
    
            <input
                id={id}
                type="time"
                {...props}
                className="border-1 border-black rounded-sm-custom cursor-pointer"
            />
            
        </div>
    )
}