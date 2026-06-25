import { SelectHTMLAttributes } from "react";

interface SelectInputProps extends SelectHTMLAttributes<HTMLSelectElement> {
    label?: string;
    options: { value: string; label: string }[];
}

export default function SelectInput({ label, id, options, ...props }: SelectInputProps) {
    return(
        <div className="flex flex-col items-start">
            {label && (
                <label htmlFor={id} className="text-h5">
                    {label}
                </label>
            )}
    
            <select
                id={id}
                {...props}
                className="border-1 border-black rounded-sm-custom bg-white cursor-pointer"
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