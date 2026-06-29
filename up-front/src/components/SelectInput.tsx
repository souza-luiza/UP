import { SelectHTMLAttributes } from "react";

interface SelectInputProps extends SelectHTMLAttributes<HTMLSelectElement> {
    label?: string;
    options: { value: string; label: string }[];
}

export default function SelectInput({ label, id, options, className, ...props }: SelectInputProps) {
    return(
        <div className="flex flex-col items-start w-full gap-1.5">
            {label && (
                <label htmlFor={id} className="text-p font-medium text-neutral-800">
                    {label}
                </label>
            )}
    
            <select
                id={id}
                {...props}
                className={`w-full py-2 px-3 border border-secondary-dark/20 rounded-sm-custom bg-white cursor-pointer text-p text-neutral-800 outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all ${className || ''}`}
            >
                <option value="" disabled>Selecione um dia</option>
                {options.map((opt) => (
                    <option key={opt.value} value={opt.value}>
                        {opt.label}
                    </option>
                ))}
            </select>
            
        </div>
    )
}