import { InputHTMLAttributes } from "react";

interface TimeInputProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type'> {
    label?: string;
}

export default function TimeInput({ label, id, className, ...props }: TimeInputProps) {
    return(
        <div className="flex flex-col items-start w-full gap-1.5">
            
            {label && (
                <label htmlFor={id} className="text-p font-semibold text-neutral-800">
                    {label}
                </label>
            )}
    
            <input
                id={id}
                type="time"
                {...props}
                className={`w-full py-2 px-3 border border-secondary-dark/20 rounded-sm-custom bg-white cursor-pointer text-p text-neutral-800 outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all ${className || ''}`}
            />
            
        </div>
    )
}