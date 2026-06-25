import { InputHTMLAttributes } from "react";

interface RadioInputProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type'> {
    label: string;
}

export default function RadioInput({ label, id, ...props }: RadioInputProps) {
    return(
        <div className="flex items-center gap-2 cursor-pointer select-none">
            <input
                id={id} 
                type="radio" 
                {...props}
                className="w-4 h-4 "
            />
            {label && (
                <label htmlFor={id} className="text-h5">
                    {label}
                </label>
            )}
        </div>
    )
}